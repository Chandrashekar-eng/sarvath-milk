package com.example.demo;
import jakarta.persistence.*;
@Entity @Table(name="site_settings")
public class SiteSettings {
 @Id private Long id=1L;
 private String phone="9618965566";
 private String email="sarvathdairy@gmail.com";
 private String location="Bijinapally";
 private String youtubeUrl="https://www.youtube.com/@Sarvath-CS";
 private String whatsappUrl="";
 private String appLink="";
 private String founderName="Yashwanth Kumar";
 private String founderPhotoUrl="";
 private String logoUrl="";
 public SiteSettings(){}
 public Long getId(){return id;} public void setId(Long v){id=v;}
 public String getPhone(){return phone;} public void setPhone(String v){phone=v;}
 public String getEmail(){return email;} public void setEmail(String v){email=v;}
 public String getLocation(){return location;} public void setLocation(String v){location=v;}
 public String getYoutubeUrl(){return youtubeUrl;} public void setYoutubeUrl(String v){youtubeUrl=v;}
 public String getWhatsappUrl(){return whatsappUrl;} public void setWhatsappUrl(String v){whatsappUrl=v;}
 public String getAppLink(){return appLink;} public void setAppLink(String v){appLink=v;}
 public String getFounderName(){return founderName;} public void setFounderName(String v){founderName=v;}
 public String getFounderPhotoUrl(){return founderPhotoUrl;} public void setFounderPhotoUrl(String v){founderPhotoUrl=v;}
 public String getLogoUrl(){return logoUrl;} public void setLogoUrl(String v){logoUrl=v;}
}
