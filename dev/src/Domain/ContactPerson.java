package Domain;

public class ContactPerson {

    private String name;
    private String phoneNumber;
    private String email;

    public ContactPerson(String name, String phoneNumber, String email) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }
    //Getters
    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    //setters
    public void setName(String name) {
        this.name = name;
    }


    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return
                "name: '" + name + '\'' +
                ", phoneNumber: '" + phoneNumber + '\'' +
                ", email: '" + email + '\'' ;
    }

    public String getAsCsvString() {
        return name + "-" + phoneNumber + "-" + email;
    }

    public static ContactPerson fromCsvString(String str) {
        String[] parts = str.split("-");
        if (parts.length != 3) throw new IllegalArgumentException("Invalid format for contact person: " + str);
        return new ContactPerson(parts[0], parts[1], parts[2]);
    }
}


