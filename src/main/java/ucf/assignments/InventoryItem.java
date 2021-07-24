package ucf.assignments;

public class InventoryItem {
    private Integer id;
    private boolean isDirty; // will be changed to true when a submission has been performed
    private String serialNumber;
    private String name;
    private Double value;
    private boolean isChanging;

    public InventoryItem(Integer id) {
        this.id = id;
        this.isDirty = false;
        this.isChanging = true;
        this.serialNumber = "";
        this.name = "";
    }

    public Integer getId() {
        return this.id;
    }

    public void markAsDirty() {
        this.isDirty = true;
    }

    public Boolean isDirty() {
        return isDirty;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) throws IllegalArgumentException {
        if (validateSerialNumber(serialNumber)) {
            this.serialNumber = serialNumber;
        } else {
            this.serialNumber = serialNumber;
            throw new IllegalArgumentException("Must be 10 characters long");
        }
    }

    public boolean validateSerialNumber(String serialNumber) {
        return serialNumber != null && serialNumber.length() == 10;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) throws IllegalArgumentException {
        if (validateName(name)) {
            this.name = name;
        } else {
            this.name = name;
            throw new IllegalArgumentException("Name must be between 2 and 256 charachters");
        }
    }

    public boolean validateName(String name) {
        return name != null && name.length() >= 2 && name.length() <= 256;
    }

    public String getValue() {
        if (value == null) {
            return "";
        } else {
            return String.format("%.2f", value);
        }
    }

    public String getFormattedValue() {
        if (value == null) {
            return "";
        } else {
            return String.format("$%.2f", value);
        }
    }

    public double getValueAsDouble() {
        return this.value;
    }

    public void setValue(Double value) throws IllegalArgumentException {
        if (this.validateValue(value)) {
            this.value = value;
        } else {
            this.value = value;
            throw new IllegalArgumentException("Value must be positive");
        }
    }

    public boolean validateValue(Double value) {
        return value >= 0.0;
    }

    public boolean getIsChanging() {
        return this.isChanging;
    }

    public void setIsChanging(boolean isChanging) {
        this.isChanging = isChanging;
    }
}
