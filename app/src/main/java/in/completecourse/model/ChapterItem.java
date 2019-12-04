package in.completecourse.model;

public class ChapterItem {

    private String chapterKaName, chapterKaFlipURL, conceptKaFlipURL, chapterKaVideoID, chapterSerial, otherImportantQues, desc;


    public ChapterItem(){

    }

    public String getChapterKaVideoID() {
        return chapterKaVideoID;
    }

    public void setChapterKaVideoID(String chapterKaVideoID) {
        this.chapterKaVideoID = chapterKaVideoID;
    }

    public void setConceptKaFlipURL(String conceptKaFlipURL) {
        this.conceptKaFlipURL = conceptKaFlipURL;
    }

    public String getOtherImportantQues() {
        return otherImportantQues;
    }

    public void setOtherImportantQues(String otherImportantQues) {
        this.otherImportantQues = otherImportantQues;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getChapterSerial() {
        return chapterSerial;
    }

    public void setChapterSerial(String chapterSerial) {
        this.chapterSerial = chapterSerial;
    }

    public void setChapterKaFlipURL(String chapterKaFlipURL) {
        this.chapterKaFlipURL = chapterKaFlipURL;
    }

    public void setChapterKaName(String chapterKaName) {
        this.chapterKaName = chapterKaName;
    }

    public String getChapterKaFlipURL() {
        return chapterKaFlipURL;
    }

    public String getChapterKaName() {
        return chapterKaName;
    }

    public String getConceptKaFlipURL() {
        return conceptKaFlipURL;
    }
}
