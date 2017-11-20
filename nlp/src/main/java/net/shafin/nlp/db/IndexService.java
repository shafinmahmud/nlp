package net.shafin.nlp.db;

import net.shafin.common.model.Document;
import net.shafin.nlp.corpus.model.TermIndex;
import net.shafin.common.util.FileUtil;
import net.shafin.nlp.main.AppBootProcess;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Shafin Mahmud
 * @since 10/2/2016
 */
public class IndexService {

    private final IndexDao dao;
    private final VerbsDao verbsDao;
    private final NounsDao nounsDao;

    public IndexService() {
        this.dao = new IndexDao(SQLiteDBHandler.getSQLiteDBConn());
        this.verbsDao = new VerbsDao(SQLiteDBHandler.getSQLiteDBConn());
        this.nounsDao = new NounsDao(SQLiteDBHandler.getSQLiteDBConn());
    }

    public void recreateIndex() {
        dao.deleteTable();
        dao.createTable();
    }

    public boolean emptyTableTermIndex() {
        return dao.truncateTermIndex();
    }

    public boolean isExistVerb(String verb) {
        return verbsDao.isExists(verb);
    }

    public boolean isExistNoun(String noun) {
        return nounsDao.isExists(noun);
    }

    public boolean isExists(int docId, String term) {
        return dao.isExistsByDocIdAndTerm(docId, term);
    }

    public boolean setAsManualKP(int docId, String term) {
        return dao.updateIsManualKP(docId, term, true);
    }

    public List<TermIndex> getTermIndexesByDocId(int docId) {
        return dao.getIndexesByDocID(docId);
    }

    public List<TermIndex> getManualIndexesByDocId(int docId) {
        return dao.getManualIndexesByDocId(docId);
    }

    public List<TermIndex> getTermIndexByDocIdAndTerm(int docId, String term) {
        return dao.getIndexesByDocIdAndTerm(docId, term);
    }

    public boolean insertIndex(TermIndex index) {
        return dao.insertTermIndex(index);
    }

    public boolean batchInsertIndex(List<TermIndex> termIndexes) {
        return dao.insertTermInBatch(termIndexes);
    }

    public int countDocs() {
        return dao.getDocCount();
    }

    public int countTrainDocs() {
        return dao.getTrainDocCount();
    }

    public int countTestDocs() {
        return dao.getTestDocCount();
    }

    public int trainTermCount() {
        return dao.getTrainTermCount();
    }

    public int testTermCount() {
        return dao.getTestTermCount();
    }

    public int termCountByDoc(int docId) {
        return dao.getTermCountByDoc(docId);
    }

    public int uniqueTermCount() {
        return dao.getDistictTermCount();
    }

    public List<Integer> getDocIds(boolean isTrain) {
        return dao.getDocIds(isTrain);
    }

    public boolean updateDF() {
        return dao.updateDF(false);
    }

    public boolean deleteTermIndex(TermIndex index) {
        return dao.deleteTermIndex(index);
    }


    public boolean updateTerm(Map<String, String> termMap) {
        return dao.updateTermByBatch(termMap);
    }

    public List<String> getDistinctTermByPage(int page, int size) {
        return dao.getDistinctTermsByPagination(page, size);
    }

    public List<String> getDistinctTermsOfDoc(int docId) {
        return dao.getDistinctTermsByDocId(docId);
    }

    public List<TermIndex> getTrainSet(int page, int size) {
        return dao.getIndexesByIsTrainPagination(true, page, size);
    }

    public List<TermIndex> getTrainSet(int docId, int page, int size) {
        return dao.getIndexesByIsTrainPagination(docId, true, page, size);
    }

    public List<TermIndex> getTestSet(int page, int size) {
        return dao.getIndexesByIsTrainPagination(false, page, size);
    }

    public List<TermIndex> getTestSet(int docId, int page, int size) {
        return dao.getIndexesByIsTrainPagination(docId, false, page, size);
    }

    public boolean enlistAsZeroFreqTerm(TermIndex index) {
        return dao.insertAsDiscardedTerm(index, new File(AppBootProcess.ZERO_FREQ_FILE));
    }

    public boolean enlistAsStopWordContainedTerm(TermIndex index) {
        return dao.insertAsDiscardedTerm(index, new File(AppBootProcess.STOP_FILTERED_FILE));
    }

    public boolean enlistAsVerbSuffixedTerm(TermIndex index) {
        return dao.insertAsDiscardedTerm(index, new File(AppBootProcess.VERB_SUFFIX_FILTERED_FILE));
    }

    public boolean writeDocumentToDisk(Document document, String fileURI) {
        try {
            return FileUtil.writeFile(fileURI, document.toJsonString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}