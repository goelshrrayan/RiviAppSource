package com.example.android.riviapp.Recycler;

public class Blog {
    String title,desc,img,card_no,details_about,details_where_name,details_where_distance,details_dishes,details_images;
    String blog;
    public Blog(String title, String desc, String img, String card_no, String details_about, String details_where_name, String details_where_distance, String details_dishes, String details_images) {
        this.title = title;
        this.desc = desc;
        this.img = img;
        this.card_no = card_no;
        this.details_about = details_about;
        this.details_where_name = details_where_name;
        this.details_where_distance = details_where_distance;
        this.details_dishes = details_dishes;
        this.details_images = details_images;
    }

    public Blog(String blog)
    {this.blog=blog;
    }

    public String getBlog()
    {return blog;
    }
    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public String getImg() {
        return img;
    }

    public String getCard_no() {
        return card_no;
    }

    public String getDetails_about() {
        return details_about;
    }

    public String getDetails_where_name() {
        return details_where_name;
    }

    public String getDetails_where_distance() {
        return details_where_distance;
    }

    public String getDetails_dishes() {
        return details_dishes;
    }

    public String getDetails_images() {
        return details_images;
    }
}
