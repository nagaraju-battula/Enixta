function processRequest(url, data, targetId) {
	/*if ($('#__ctoken') === undefined) {
		data.__ctoken = $('#__ctoken').val();
	}*/
	var promodomain =  'http://' + window.location.host;
    jQuery.ajax({
        type: "POST",
        url: promodomain + url,
        data: data,
        contentType: "application/json; charset=UTF-8",
        success: function (result) {
            if (result != null) {
                $("#searchResultdiv").empty();
                $("#" + targetId).empty().append(result);
            } else if (result.status == "FAILURE") {
                $('#errorMessage').text("error in emailID updating");
            }
        }
    });
}

function OpenInNewTab() {

    var win = window.open('D://mysql.stx', '_blank');
    win.focus();
}

function submitForm(url, formId, targetId) {
	showLoadingSpinner(formId, targetId);
    if ('NO-DATA' != formId && isFormValid(formId)) {
        formData = JSON.stringify($("#" + formId).serializeObject());
        processRequest(url, formData, targetId);
    } else if ('NO-DATA' == formId) {
        var formData = {};
        processRequest(url, formData, targetId);
    } else if ('searchFormByDate' == formId) {
        formData = JSON.stringify($("#" + formId).serializeObject());
        processFileRequest(url, formData, targetId);
    }
    //hideLoadingSpinner(targetId);
}

function showLoadingSpinner(formId, targetId){
	if('loginform' == formId){
		//$("#loginspinner").html("<div class='throbber-loader'></div>");
	}else{
		$('.overlay').remove();
		$("#" + targetId).append("<div class='overlay'><i class='fa fa-refresh fa-spin'></i></div>");
	}	
}

$.fn.serializeObject = function () {
    "use strict";
    var result = {};
    var subResult = [];
    var arrayIndex = 0;

    var extend = function (i, element) {
        try {
            var name = element.name;
            var value = element.value;
            /*
             * All the node names contains . should be in this format
             * form.clinic.name
             */
            if (name.indexOf('.') > -1) {
                var property = name.substring(0, name.lastIndexOf("."));
                subResult[arrayIndex++] = property;
            } else {
                result[name] = value;
            }
        } catch (err) {
            console.log("Error" + err);
        }
    };
    /* Extend Function to get the json of any kind of form */
    $.each(this.serializeArray(), extend);

    if (subResult.length > 0) {
        var uniqueElements = $.unique(subResult);
        $.each(uniqueElements, function (index, value) {
            var childNodes = {};
            $.each($("[name^='" + value + "']"), function (k, v) {
                if(v.type == "checkbox"){
                	if(v.checked){
		            	var nodeName = v.name;
		                var node = nodeName.substring(nodeName.lastIndexOf(".") + 1,
		                    nodeName.length);
		                childNodes[node] = v.value;
                	}
                }else{
                	var nodeName = v.name;
	                var node = nodeName.substring(nodeName.lastIndexOf(".") + 1,
	                    nodeName.length);
	                childNodes[node] = v.value;
                }
            });
            var beanNode = value
                .substring(value.indexOf(".") + 1, value.length);
            result[beanNode] = childNodes;
        });
    }
    return result;
};

/*
 * Code to hide error divs 
 * 
 */
$(document).ready(function () {
    $("#resultDev").hide();
});

/*
 * Code for Drop Down
 */
$(document.body).on(
    'click',
    '.dropdown-menu li',
    function (event) {
        var $target = $(event.currentTarget);
        $target.closest('.btn-group').find('[data-bind="label"]').text(
            $target.text()).end().children('.dropdown-toggle')
            .dropdown('toggle');

        return false;

    });

function isFormValid(formId) {
    var isValid = true;
    $('li', $(this).parents('form')).removeClass('has-error');
    $('label.has-error').remove();

    //Cleanup all the previous error messages
    $('.error-msg').remove();
    $('#'+formId+' .has-error').each(function () {
         $(this).removeClass('has-error');
    });
    
    $('#'+formId+' .required').each(function () {
        if ($(this).val() == '') {
            isValid = false;
            $(this).parent().addClass('has-error').append('<label class="control-label error-msg" for="inputError"><i class="fa fa-times-circle-o"></i> Input with error</label>');
        }
    });
    $('#'+formId+' .onlyNumber').each(function () {
        if (!isANumber($(this).val())) {
            isValid = false;
            $(this).parent().addClass('has-error').append('<label class="control-label error-msg" for="inputError"><i class="fa fa-times-circle-o"></i>Required only Number</label>');
        }
    });
    
    $('#'+formId+' .aadhaarNumber').each(function () {
        if (!isANumber($(this).val()) || !isOfLength($(this).val(), 12)) {
            isValid = false;
            $(this).parent().addClass('has-error').append('<label class="control-label error-msg" for="inputError"><i class="fa fa-times-circle-o"></i>Aadhaar should be a 12 digit number</label>');
        }
    });
    
    $('#'+formId+' .onlyName').each(function () {
        if (!isANumber($(this).val())) {
            isValid = false;
            $(this).parent().addClass('has-error').append('<label class="control-label error-msg" for="inputError"><i class="fa fa-times-circle-o"></i>Required only Number</label>');
        }
    });
    
    $('#'+formId+' .onlyMobileNum').each(function () {
        if (!isOfLength($(this).val(), 10) || !isANumber($(this).val())) {
            isValid = false;
            $(this).parent().addClass('has-error').append('<label class="control-label error-msg" for="inputError"><i class="fa fa-times-circle-o"></i>Mobile number must be a number with size 10.</label>');
        }
    });
 
    return isValid;
}

function isOfLength(str, length) {
    if (str.length == length) {
        return true;
    }
    return false;
}

function isANumber(numStr) {
    var regExp = /^[0-9]+$/;
    if (regExp.test(numStr)) {
        return true;
    }
    return false;
}

function submitAsPost(url, videoId, deviceName, targetId) {
    
	var formData = '{"videoId":"'+videoId+'", "deviceName":"'+deviceName+'"}';
    
    processRequest(url, formData, targetId);
}
