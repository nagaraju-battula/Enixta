(function () {

    var run,
        destroying$;

    var run = function (fn) {
        if (fn && typeof fn === 'function') {
            return fn();
        }
    };

    Ember.View.reopen({

        isAnimatingIn : false,
        isAnimatingOut : false,
        hasAnimatedIn : false,
        hasAnimatedOut : false,

        _animateInCallbacks : null,
        _animateOutCallbacks : null,

        _afterRender : function () {

            var self = this;

            this.$el = this.$();

            if (!self.isDestroyed) {

                self.willAnimateIn();
                self.isAnimatingIn = true;
                self.hasAnimatedIn = false;

                Ember.run.next(function () {

                    if (!self.isDestroyed) {

                        self.animateIn(function () {

                            var i;

                            self.isAnimatingIn = false;
                            self.hasAnimatedIn = true;
                            self.didAnimateIn();

                            if (self._animateInCallbacks && self._animateInCallbacks.length) {
                                for (i = 0; i < self._animateInCallbacks.length; i ++) {
                                    run(self._animateInCallbacks[i]);
                                }
                            }

                            self._animateInCallbacks = null;

                        });
                    }
                });
            }
        },

        willInsertElement : function () {
            Ember.run.scheduleOnce('afterRender', this, this._afterRender);
            return this._super();
        },

        willAnimateIn : Ember.K,
        willAnimateOut : Ember.K,
        didAnimateIn : Ember.K,
        didAnimateOut : Ember.K,

        animateIn : run,
        animateOut : run,

        onAnimateIn : function (callback) {

            this._animateInCallbacks = this._animateInCallbacks || [];

            if (typeof callback === 'function') {
                this._animateInCallbacks.push(callback);
            }
        },

        onAnimateOut : function (callback) {

            this._animateOutCallbacks = this._animateOutCallbacks || [];

            if (typeof callback === 'function') {
                this._animateOutCallbacks.push(callback);
            }
        },

        destroy : function (done) {

            var _super = this._super;

            /**
             * LGT 20140630 HACK to properly call destroy; see comment in animateOut() callback.
             */
            if (this.reallyDestroy){
                _super.call(this);
                return;
            }

            this.onAnimateOut(done);

            if (this.isAnimatingOut) {
                return;
            }

            if (!this.$el || this.isDestroyed) {

                if (this._animateOutCallbacks && this._animateOutCallbacks.length) {
                    for (i = 0; i < this._animateOutCallbacks.length; i ++) {
                        run(this._animateOutCallbacks[i]);
                    }
                }

                this._animateOutCallbacks = null;

                return _super.call(this);
            }

            if (!this.$()) {
                this.$ = function () {
                    return this.$el;
                }
            }

            this.willAnimateOut();
            this.isAnimatingOut = true;

            this.animateOut(function () {

                this.isAnimatingOut = false;
                this.hasAnimatedOut = true;

                this.didAnimateOut();

                var runningAnimateOutCallback = false;
                if (this._animateOutCallbacks && this._animateOutCallbacks.length) {
                    for (i = 0; i < this._animateOutCallbacks.length; i ++) {
                        runningAnimateOutCallback = true;
                        run(this._animateOutCallbacks[i]);
                    }
                }

                this.isDestroying = runningAnimateOutCallback; //this.hasAnimatedOut;

                /*
                 LGT 20140630 replaced _super.call(this) with this, because in certain cases, _super.call() *inside this
                 callback* will result in calling animatedOut() instead of destroy(); not sure if this is Ember's fault,
                 or ember-animate making false assumptions about _super behavior (probably the latter).

                 Therefore, in order to make sure destroy() is called, I'm using a flag.  When the flag is detected above
                 in the destroy() body, it will call _super.call(), which will correctly call the parent destroy().
                 */
                //_super.call(this);
                this.reallyDestroy = true;
                this.destroy();

                /*
                 LGT 20140626
                 this block is cleaning up by hand since we are animating something (don't quite get this yet...);
                 otherwise, _super() calls the destroy sequence normally and it works
                 */
                if (runningAnimateOutCallback) {
                    // destroy the element -- this will avoid each child view destroying
                    // the element over and over again...
                    if (!this.removedFromDOM) {
                        this.destroyElement();
                    }

                    // remove from parent if found. Don't call removeFromParent,
                    // as removeFromParent will try to remove the element from
                    // the DOM again.

                    if (this._parentView) {
                        this._parentView.removeChild(this);
                    }

                    this.isDestroying = true;

                    this.transitionTo('destroying', false);
                }

                delete this.$;
                delete this.$el;

                return this;

            }.bind(this));

            return this;
        }
    });

    Ember.ContainerView.reopen({

        currentView : null,
        activeView : null,
        newView : null,
        nextView : null,

        animationSequence : 'sync', // sync, async, reverse

        init : function () {

            var currentView;

            this._super();

            if (currentView = this.get('currentView')) {
                this.set('activeView', currentView);
            }
        },

        _currentViewWillChange : Ember.K,

        _currentViewDidChange : Ember.observer('currentView', function () {

            var self,
                newView,
                oldView,
                asyncCount;

            self = this;
            oldView = this.get('activeView');
            newView = this.get('currentView');

            this.set('newView', newView);

            function pushView (view) {

                if (view ) {
                    self.pushObject(view);
                }

                self.set('activeView', view);
            }

            function removeView (view) {

                if (view.isAnimatingOut) {
                    return;
                }

                if (!view.hasAnimatedIn) {
                    view.onAnimateIn(view.destroy.call(view));
                    return;
                }

                view.destroy();
            };

            if (oldView) {

                // reverse
                if (this.animationSequence === 'reverse') {

                    newView.onAnimateIn(function () {
                        removeView(oldView);
                    });

                    pushView(newView);
                }

                // async
                else if (this.animationSequence === 'async') {
                    removeView(oldView);
                    pushView(newView);
                }

                // sync
                else {

                    oldView.onAnimateOut(function () {
                        pushView(self.get('currentView'));
                    });

                    removeView(oldView);
                }
            }

            else {
                pushView(newView);
            }
        })

    });

    /******************************************************
     * LGT 20140618 Added the following to make animateOut
     * hooks work for metamorph (e.g. {{#if something}}) views.
     *
     * There's a hack at the end which waits 500ms before destroying any child views
     * that are animating out.
     *
     * The rest of the _MetamorphView.domManager should match Ember source.
     */

    function notifyMutationListeners() {
        Ember.run.once(Ember.View, 'notifyMutationListeners');
    }

    Ember._MetamorphView.reopen({

        init: function(){
            this._super();
            Ember.merge(this.domManager, this._domManager);
        },

        _domManager: {
            hasAnimatingChild: function(view){
                var childViews = view._childViews, childViewIsAnimating = false, i;
                var len = childViews.length;

                for (i = 0; i < len; i++) {
                    if (childViews[i].isAnimatingOut) {
                        childViewIsAnimating = true;
                        break;
                    }
                }

                return childViewIsAnimating;
            },

            // This is messed up.
            replace: function(view) {
                var morph = view.morph;

                view.transitionTo('preRender');

                var renderMetamorphView = function(){
                    if (view.isDestroying) { return; }

                    view.clearRenderedChildren();
                    var buffer = view.renderToBuffer();

                    view.invokeRecursively(function(view) {
                        view.propertyWillChange('element');
                    });
                    view.triggerRecursively('willInsertElement');

                    morph.replaceWith(buffer.string());
                    view.transitionTo('inDOM');

                    view.invokeRecursively(function(view) {
                        view.propertyDidChange('element');
                    });
                    view.triggerRecursively('didInsertElement');

                    notifyMutationListeners();
                }

                // HACK (7/10) wait 500ms to clean up the view, to allow animations to finish
                if (this.hasAnimatingChild(view)){
                    Ember.run.later(function(){
                        Ember.run.schedule('render', this, renderMetamorphView);
                    }, 500);
                }
                else{
                    Ember.run.schedule('render', this, renderMetamorphView);
                }
            }
        }
    });

})();