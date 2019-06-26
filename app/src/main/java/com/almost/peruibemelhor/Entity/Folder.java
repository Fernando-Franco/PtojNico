package com.almost.peruibemelhor.Entity;

public class Folder {

    private String id_folder;
    private String id_creator;

    private String title;
    private String photo;
    private String description;

    private boolean following;
    private boolean mute;

    public Folder(){}

    public Folder(String id_folder, String id_creator, String title, String photo, String description) {
        this.id_folder = id_folder;
        this.id_creator = id_creator;
        this.title = title;
        this.photo = photo;
        this.description = description;
    }

    public String getId_folder() {
        return id_folder;
    }

    public void setId_folder(String id_folder) {
        this.id_folder = id_folder;
    }

    public String getId_creator() {
        return id_creator;
    }

    public void setId_creator(String id_creator) {
        this.id_creator = id_creator;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isFollowing() {
        return following;
    }

    public void setFollowing(boolean following) {
        this.following = following;
    }

    public boolean isMute() {
        return mute;
    }

    public void setMute(boolean mute) {
        this.mute = mute;
    }
}
