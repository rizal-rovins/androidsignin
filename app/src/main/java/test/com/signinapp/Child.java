package test.com.signinapp;

/**
 * Created by JAYAN on 2/11/2017.
 */

public class Child
{
    public int icon;
    public String title;
    public String dob;
    public String sex;
    public int childid;
    public Child(){
        super();
    }

    public Child(int icon, int childid,String title,String dob, String sex) {
        super();
        this.icon = icon;
        this.title = title;
        this.dob=dob;
        this.childid=childid;
        this.sex=sex;
    }
    public String getTitle()
    {return title;}
    public int getIcon()
    {return icon;}
    public int getChildid()
    {return childid;}
}
