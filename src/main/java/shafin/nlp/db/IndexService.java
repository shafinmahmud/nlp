package shafin.nlp.db;

import java.io.IOException;
import java.util.List;

import shafin.nlp.corpus.model.Document;
import shafin.nlp.util.FileHandler;

public class IndexService {

	private final IndexDao dao;

	public IndexService() {
		this.dao = new IndexDao(SQLiteDBConn.getSQLiteDBConn());
	}

	public void recreatIndex() {
		dao.deleteTable();
		dao.createTable();
	}

	public boolean isExists(int docId, String term){
		return dao.isExistsByDocIdAndTerm(docId, term);
	}
	
	public boolean setAsManualKP(int docId, String term){
		return dao.updateIsManualKP(docId, term, true);
	}
	
	public List<TermIndex> getIndexTerm(int docId) {
		return dao.getIndexesByDocID(docId);
	}

	public boolean insertIndex(TermIndex index){
		return dao.insertTerm(index.getDocId(), index.getTerm(), index.getTf(), index.getPs());
	}

	public boolean batchInsertIndex(List<TermIndex> termIndexes) {
		return dao.insertTermInBatch(termIndexes);
	}

	public int countDocs() {
		return dao.getDocCount();
	}

	public boolean updateDF() {
		return dao.updateDF(false);
	}

	public boolean enlistAsZeroFreqTerm(TermIndex index) {
		try {
			return dao.insertAsDiscardedTerm(index, IndexDao.zeroFreqFile);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean enlistAsStopWordContainedTerm(TermIndex index) {
		try {
			return dao.insertAsDiscardedTerm(index, IndexDao.stopFilteredFile);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean enlistAsVerbSuffixedTerm(TermIndex index) {
		try {
			return dao.insertAsDiscardedTerm(index, IndexDao.verbSuffxFilteredFile);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean writeDocumentToDisk(Document document, String fileURI){
		try {
			return FileHandler.writeFile(fileURI, document.toJsonString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
}
