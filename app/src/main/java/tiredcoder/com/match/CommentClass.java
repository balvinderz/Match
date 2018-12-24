package tiredcoder.com.match;

import android.graphics.Bitmap;

public class CommentClass {
    int commenterid,postid;
    String image;
    String comment,name;
CommentClass()
{}

    public CommentClass(int commenterid, int postid, String image, String comment, String name) {
        this.commenterid = commenterid;
        this.postid = postid;
        this.image = image;
        this.comment = comment;
        this.name = name;
    }

    public int getCommenterid() {
        return commenterid;
    }

    public void setCommenterid(int commenterid) {
        this.commenterid = commenterid;
    }

    public int getPostid() {
        return postid;
    }

    public void setPostid(int postid) {
        this.postid = postid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
