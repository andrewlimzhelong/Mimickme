package com.example.andrewspc.mimickme.Explore;

public class ExploreModelClass {

    private String Username;
    private String ProfilePicture;
    private String UserID;
    private String ContactNumber;
    private String Occupation;
    private String Description;
    private String PostedImage;

    public ExploreModelClass() {
    }

    public ExploreModelClass(String Username, String ProfilePicture, String UserID, String ContactNumber, String Occupation,
                             String Description, String PostedImage) {

        this.Username = Username;
        this.ProfilePicture = ProfilePicture;
        this.ProfilePicture = UserID;
        this.ContactNumber = ContactNumber;
        this.Occupation = Occupation;
        this.Description = Description;
        this.PostedImage = PostedImage;
    }

    public String getProfilePicture() {
        return ProfilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        ProfilePicture = profilePicture;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getContactNumber() {
        return ContactNumber;
    }

    public void setContactNumber(String contactNumber) {
        ContactNumber = contactNumber;
    }

    public String getOccupation() {
        return Occupation;
    }

    public void setOccupation(String occupation) {
        Occupation = occupation;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getPostedImage() {
        return PostedImage;
    }

    public void setPostedImage(String postedImage) {
        PostedImage = postedImage;
    }
}
