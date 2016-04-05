/**
 * 
 */
package com.enixta.platform.buysmaart.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MultiTermQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.search.spans.SpanMultiTermQueryWrapper;
import org.apache.lucene.search.spans.SpanOrQuery;
import org.apache.lucene.search.spans.SpanQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import com.enixta.platform.buysmaart.model.YoutubeVideoReview;

/**
 * @author nbattula
 *
 */
public class LuceneSearch {

	public final static String FUZZY_PERCENT = "~8.0";
	private static final String TERM_FIELD = "title";
	private static Directory index = null;

	public static void main(String[] args) throws IOException, ParseException {

		StandardAnalyzer analyzer = new StandardAnalyzer();

		// 1. create the index
		Directory index = new RAMDirectory();

		IndexWriterConfig config = new IndexWriterConfig(analyzer);

		IndexWriter w = new IndexWriter(index, config);
		addDoc(w, "Lucene in Action", "101");
		addDoc(w, "Lucene for Dummies", "102");
		addDoc(w, "Managing Gigabytes", "103");
		addDoc(w, "The Art of Computer Science", "104");
		w.close();

		String querystr = "Science";

		StringTokenizer st = new StringTokenizer(querystr, " ");
		List<String> queryStrings = new ArrayList<String>();

		while (st.hasMoreElements()) {
			queryStrings.add((String) st.nextElement());
		}

		SpanQuery[] clauses = new SpanQuery[queryStrings.size()];

		for (int i = 0; i < queryStrings.size(); i++) {
			FuzzyQuery fuzzyQuery = new FuzzyQuery(new Term(TERM_FIELD,
					queryStrings.get(i)), 1);
			clauses[i] = new SpanMultiTermQueryWrapper<MultiTermQuery>(
					fuzzyQuery);
		}

		SpanOrQuery query = new SpanOrQuery(clauses);

		// 3. search
		int hitsPerPage = 10;
		IndexReader reader = DirectoryReader.open(index);
		IndexSearcher searcher = new IndexSearcher(reader);
		TopScoreDocCollector collector = TopScoreDocCollector
				.create(hitsPerPage);
		searcher.search(query, collector);
		ScoreDoc[] hits = collector.topDocs().scoreDocs;

		// 4. display results
		System.out.println("Found " + hits.length + " hits.");
		for (int i = 0; i < hits.length; ++i) {
			int docId = hits[i].doc;
			Document d = searcher.doc(docId);
			System.out.println((i + 1) + ". " + d.get("isbn") + "\t"
					+ d.get(TERM_FIELD));
		}

		reader.close();
	}

	public static List<String> search(String querystr, List<YoutubeVideoReview> reviews) throws IOException,
			ParseException {

		StringTokenizer st = new StringTokenizer(querystr, " ");
		List<String> queryStrings = new ArrayList<String>();

		while (st.hasMoreElements()) {
			queryStrings.add((String) st.nextElement());
		}

		SpanQuery[] clauses = new SpanQuery[queryStrings.size()];

		for (int i = 0; i < queryStrings.size(); i++) {
			FuzzyQuery fuzzyQuery = new FuzzyQuery(new Term(TERM_FIELD,
					queryStrings.get(i)), 2);
			clauses[i] = new SpanMultiTermQueryWrapper<MultiTermQuery>(
					fuzzyQuery);
		}

		SpanOrQuery query = new SpanOrQuery(clauses);

		// 3. search
		int hitsPerPage = 10;
		IndexReader reader = DirectoryReader.open(index);
		IndexSearcher searcher = new IndexSearcher(reader);
		TopScoreDocCollector collector = TopScoreDocCollector
				.create(hitsPerPage);
		searcher.search(query, collector);
		ScoreDoc[] hits = collector.topDocs().scoreDocs;

		// 4. display results
		List<String> results = new ArrayList<String>();

		results.add("Found " + hits.length + " hits.");

		for (int i = 0; i < hits.length; ++i) {
			int docId = hits[i].doc;
			Document d = searcher.doc(docId);

			results.add((i + 1) + ". Video ID : " + d.get("isbn") + "\t"
					+ " -> Title : "+d.get(TERM_FIELD));
		}

		reader.close();

		return results;
	}

	public static void init(List<YoutubeVideoReview> reviews){
		
		StandardAnalyzer analyzer = new StandardAnalyzer();
		
		if(index == null){
			index = new RAMDirectory();
		}
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		IndexWriter w;
		try {
			w = new IndexWriter(index, config);
			loadDataFromDB(reviews, w);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	private static void loadDataFromDB(List<YoutubeVideoReview> reviews, IndexWriter w){
		
		try {
			for (YoutubeVideoReview youtubeVideoReview : reviews) {
				addDoc(w, youtubeVideoReview.getReviewTitle().toLowerCase(),
						youtubeVideoReview.getVideoId());
			}
			w.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private static void addDoc(IndexWriter w, String title, String isbn)
			throws IOException {
		Document doc = new Document();
		doc.add(new TextField(TERM_FIELD, title, Field.Store.YES));

		doc.add(new StringField("isbn", isbn, Field.Store.YES));
		w.addDocument(doc);
	}
}