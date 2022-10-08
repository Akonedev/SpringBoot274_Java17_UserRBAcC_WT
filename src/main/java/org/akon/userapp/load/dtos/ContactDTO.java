package org.akon.userapp.load.dtos;

import org.akon.userapp.security.user.Contact;
import lombok.Data;

@Data
public class ContactDTO implements java.io.Serializable {

    public ContactDTO() {
        // empty constructor
    }

    public ContactDTO(Contact contact) {
        if (contact != null) {
            this.email = contact.getEmail();
            this.phone = contact.getPhone();
            this.skype = contact.getSkype();
            this.facebook = contact.getFacebook();
            this.linkedin = contact.getLinkedin();
            this.website = contact.getWebsite();
            this.contactNote = contact.getNote();
        }
    }

    private String email;
    private String phone;
    private String skype;
    private String facebook;
    private String linkedin;
    private String website;
    private String contactNote;

}
