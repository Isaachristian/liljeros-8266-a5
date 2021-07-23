package ucf.assignments;

public class InventoryItem {
    private Integer id;
    private boolean isDirty; // will be changed to true when a submission has been performed
    private String serialNumber;
    private String name;
    private Double value;
    private boolean isChanging;

    public InventoryItem(Integer id) {
        
    }

    public void setAsDirty() {

    }

    public String getSerialNumber() {

        return "";
    }

    public void setSerialNumber(String serialNumber) {

    }

    public boolean validateSerialNumber() {

        return true;
    }

    public String getName() {

        return null;
    }

    public void setName(String name) {

    }

    public boolean validateName() {

        return true;
    }

    public Double getValue() {

        return null;
    }

    public void setValue(Double value) {

    }

    public boolean validateValue() {

        return true;
    }

    public boolean getIsChanging() {

        return true;
    }

    public void setIsChanging(boolean isChanging) {

    }
}
