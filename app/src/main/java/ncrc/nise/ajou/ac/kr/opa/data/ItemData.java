package ncrc.nise.ajou.ac.kr.opa.data;

/**
 * Created by kwanhong on 2016-01-05.
 */
public class ItemData {


    private String title;
    private String calory;
    private int imageUrl;

    public ItemData(String title,int imageUrl){

        this.title = title;
        this.imageUrl = imageUrl;
    }
    // getters & setters

    public String getTitle() {
        return title;
    }

    public int getImageUrl() {
        return imageUrl;
    }
}