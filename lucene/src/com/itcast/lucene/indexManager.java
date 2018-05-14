package com.itcast.lucene;


import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;



import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class indexManager {
   
	
	
	
	//ɾ��
	
	@Test
	public void testDeleteIndex() throws Exception {
	//	1��ָ���������Ŀ¼
		FSDirectory directory = FSDirectory.open(new File("D:\\\\indexRespo\\\\11111"));
		IndexWriterConfig config = new IndexWriterConfig(Version.LATEST, new IKAnalyzer());
		IndexWriter indexWriter =new IndexWriter(directory, config);
	indexWriter.deleteAll(); // ���ã���������
		//indexWriter.deleteDocuments(new Term("name","apache"));
		indexWriter.close();
		
	}
	
	//�޸�
	
	@Test
	public void testUpdateIndex() throws Exception {
	//	1��ָ���������Ŀ¼
		FSDirectory directory = FSDirectory.open(new File("D:\\\\indexRespo\\\\11111"));
		IndexWriterConfig config = new IndexWriterConfig(Version.LATEST, new IKAnalyzer());
		IndexWriter indexWriter =new IndexWriter(directory, config);

		Document doc = new Document();
		TextField nameField  = new TextField("name", "�Լ���IndexWriterConfigһ��������ӵ�һ���ĵ�spring",Store.YES);
		nameField.setBoost(10);
		doc.add(nameField);
		doc.add(new StoredField("path", "d://sdsds"));
		doc.add(new LongField("size", 100l,Store.YES));
		doc.add(new StringField("content", "�Լ���ӵ�һ���ĵ��Լ���ӵ�һ���ĵ��Լ���ӵ�һ���ĵ�",Store.NO));
		indexWriter.updateDocument(new Term("name","apache"), doc);
		
		indexWriter.close();
		
	}
	
	
	@Test
	public void testAddIndex() throws Exception {
    	//1��ִ���������Ŀ¼
    	FSDirectory directory  = FSDirectory.open(new File("D:\\indexRespo\\11111"));
    	//2��ָ���ִ���  --��׼�ִ���
    	Analyzer analyzer =new IKAnalyzer();
		
        //3������һ�����ö���
    	IndexWriterConfig config = new IndexWriterConfig(Version.LATEST, analyzer);
    	//4������һ�� д����������
    	IndexWriter indexWriter=new IndexWriter(directory,config);
    	
    	
        //5��д�������
    	
    	File files = new File("E:\\jiuyebandeshiping\\luceneAndsolor\\lucene��һ��\\Lucene&solr-day01\\����\\�Ͽ��õĲ�ѯ����searchsource");
    	File[] listFiles = files.listFiles();
    	for (File file : listFiles) {
    		Document doc = new Document();
    		//�ļ�����
    		TextField  fileNameField = new TextField("name", file.getName(), Store.YES);
    		doc.add(fileNameField);
			//�ļ�·��
    		StoredField filePathField = new StoredField("path", file.getPath());
		         doc.add(filePathField);
		     //	�ļ���С  ��λ b    
    	long sizeOf = FileUtils.sizeOf(file);
    	LongField fileSizeField = new LongField("size",sizeOf , Store.YES);
    	doc.add(fileSizeField);
		     //	�ļ�����
		         String fileContent = FileUtils.readFileToString(file);
		     	TextField  fileContentField = new TextField("content", fileContent, Store.YES);
				doc.add(fileContentField);
				indexWriter.addDocument(doc);
		         
    	}

//		6���ر�IndexWriter����
		indexWriter.close();
		
		
    	
	}
	
	
	//��ѯ
	@Test
	public void testSearchIndex() throws Exception {
		//1��ָ���������Ŀ¼
		FSDirectory directory = FSDirectory.open(new File("D:\\indexRespo\\11111"));
//		2������һ����ȡ��������
		DirectoryReader indexReader = DirectoryReader.open(directory);
		
//		3������һ�����������Ķ���
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);
//		4��ִ�в�ѯ
		//TermQuery query = new TermQuery(new Term("content", "spring is a project"));
	//	 NumericRangeQuery<Long> query = NumericRangeQuery.newLongRange("size", 0l, 100l, true, true);
		//��ϲ�ѯ
	/*	BooleanQuery query = new BooleanQuery();
		TermQuery query1 = new TermQuery(new Term("name","spring"));
		TermQuery query2 = new TermQuery(new Term("content","spring"));
		query.add(query1,Occur.SHOULD);
		query.add(query2,Occur.SHOULD);*/
		//��ѯ����
		//Query query=new MatchAllDocsQuery();
		//�ִʲ�ѯ
		//QueryParser queryParser = new QueryParser("name", new IKAnalyzer());
		//Query query = queryParser.parse("spring is a project");
		TermQuery query = new TermQuery(new Term("name","spring"));
		TextField nameField = new TextField("name", "�Լ���IndexWriterConfigһ��������ӵ�һ���ĵ�spring",Store.YES);
		nameField.setBoost(100);
		TopDocs topDocs = indexSearcher.search(query, 100);
		System.out.println("������"+topDocs.totalHits);
		ScoreDoc[] scoreDocs = topDocs.scoreDocs;
		for (ScoreDoc scoreDoc : scoreDocs) {
			int docId = scoreDoc.doc;
			Document doc = indexSearcher.doc(docId);
			System.out.println(doc.get("name"));
			//System.out.println(doc.get("size"));
			//System.out.println(doc.get("path"));
//			System.out.println(doc.get("content"));
			System.out.println("----------------------------------------------");
		}
	//	5���ر���Դ
		indexReader.close();
	}
	
	
	//���ķִ���
	@Test
	public void testAnalyzer() throws Exception {
		Analyzer analyzer = new IKAnalyzer();
		String str = "���ǲ��ͣ�MyBatis ����apache��һ����Դ��ĿiBatis,���ֹ� 2010�������Ŀ��apache software foundation Ǩ�Ƶ���google code�����Ҹ���ΪMyBatis--by �����ɾ� ë����";		
	  	TokenStream tokenStream = analyzer.tokenStream("test", str);
		tokenStream.reset();  //����ָ��
//		���һ������ �ַ���
		CharTermAttribute addAttribute = tokenStream.addAttribute(CharTermAttribute.class);
		while(tokenStream.incrementToken()) {
			System.out.println(addAttribute);
		}
	}
	
	
}
