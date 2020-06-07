package com.learning.skilclasses.models;

import java.io.Serializable;
import java.util.Map;

public class Video implements Serializable {
    /*  private String title;
      private String url,name,vofclass,vsub,date,vid;

      public String getTitle() {
          return title;
      }

      public Video(String title, String url,String name,String vid,String date,String vofclass,String vsub) {
          this.title = title;
          this.url = url;
          this.name=name;
          this.date=date;
          this.vid=vid;
          this.vsub=vsub;
          this.vofclass=vofclass;
      }
      public String getVid(String vid){
          return vid;
      }
      public void setVid(String vid){this.vid=vid;}
      public String getName(String name){return name;}
      public void setName(String name){this.name=name;}
      public String getVofclass(String vofclass){return vofclass;}
      public void setVofclass(String vofclass){this.vofclass=vofclass;}
      public String getDate(String date){return date;}
      public void setDate(String date){this.date=date;}
      public String getVsub(String vsub){return vsub;}
      public void setVsub(String vsub){
          this.vsub=vsub;
      }
      public void setTitle(String title) {
          this.title = title;
      }

      public String getUrl() {
          return url;
      }

      public void setUrl(String url) {
          this.url = url;
      }

      public Video(String vid) {

      }*/
    private String vid;
    private String vclass;
    private String vcategory;
    private String vsubject;
    private String vdesp;
    private String vfile;
    private String vfileextension;
    private String vdate;
    private String vUrl;

    public String getThumbnailimg() {
        return thumbnailimg;
    }

    public void setThumbnailimg(String thumbnailimg) {
        this.thumbnailimg = thumbnailimg;
    }

    public Map<String, String> getUrlList() {
        return urlList;
    }

    public void setUrlList(Map<String, String> urlList) {
        this.urlList = urlList;
    }

    private String thumbnailimg;
    Map<String, String> urlList;

    public Video() {
    }

    public Video(String vid, String vclass, String vcategory, String vsubject, String vdesp, String vfile, String vfileextension, String vdate) {
        this.vid = vid;
        this.vclass = vclass;
        this.vcategory = vcategory;
        this.vsubject = vsubject;
        this.vdesp = vdesp;
        this.vfile = vfile;
        this.vfileextension = vfileextension;
        this.vdate = vdate;

    }

    public Video(String vid, String vclass, String vcategory, String vsubject, String vdesp, String vfile, String vfileextension, String vdate, String vUrl, String thumbnailimg) {
        this.vid = vid;
        this.vclass = vclass;
        this.vcategory = vcategory;
        this.vsubject = vsubject;
        this.vdesp = vdesp;
        this.vfile = vfile;
        this.vfileextension = vfileextension;
        this.vdate = vdate;
        this.vUrl = vUrl;
        this.thumbnailimg = thumbnailimg;

    }

    public Video(String vid, String vclass, String vcategory, String vsubject, String vdesp, String vfile, String vfileextension, String vdate, String vUrl, String thumbnailimg, Map<String, String> urlList) {
        this.vid = vid;
        this.vclass = vclass;
        this.vcategory = vcategory;
        this.vsubject = vsubject;
        this.vdesp = vdesp;
        this.vfile = vfile;
        this.vfileextension = vfileextension;
        this.vdate = vdate;
        this.vUrl = vUrl;
        this.thumbnailimg = thumbnailimg;
        this.urlList = urlList;

    }

    public String getvUrl() {
        return vUrl;
    }

    public void setvUrl(String vUrl) {
        this.vUrl = vUrl;
    }

    public String getVfile() {
        return vfile;
    }

    public void setVfile(String vfile) {
        this.vfile = vfile;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }


    public String getVclass() {
        return vclass;
    }

    public void setVclass(String vclass) {
        this.vclass = vclass;
    }

    public String getVcategory() {
        return vcategory;
    }

    public void setVcategory(String vcategory) {
        this.vcategory = vcategory;
    }

    public String getVsubject() {
        return vsubject;
    }

    public void setVsubject(String vsubject) {
        this.vsubject = vsubject;
    }

    public String getVdesp() {
        return vdesp;
    }

    public void setVdesp(String vdesp) {
        this.vdesp = vdesp;
    }

    public String getVfileextension() {
        return vfileextension;
    }

    public void setVfileextension(String vfileextension) {
        this.vfileextension = vfileextension;
    }

    public String getVdate() {
        return vdate;
    }

    public void setVdate(String vdate) {
        this.vdate = vdate;
    }

}
