package de.bitvale.common.phone;

public class PhoneNumber {

    private String country;

    private String countryCode;

    private String area;

    private String areaCode;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    @Override
    public String toString() {
        return "PhoneNumber{" +
                "country='" + country + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", area='" + area + '\'' +
                ", areaCode='" + areaCode + '\'' +
                '}';
    }
}
