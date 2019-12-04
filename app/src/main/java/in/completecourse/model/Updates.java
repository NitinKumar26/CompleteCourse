package in.completecourse.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by belal on 19/4/17.
 */

public class Updates {


    @SerializedName("data")
    private ArrayList<Update> heros;

    public Updates(){

    }

    public ArrayList<Update> getHeros(){
        return heros;
    }
}
