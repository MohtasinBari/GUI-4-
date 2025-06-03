package Service;

public class PaymentService {
    public boolean validateBkashNagad(String number) {
        return number.matches("017\\d{8}");
    }

    public boolean validateCard(String number) {
        return number.matches("54\\d{10}");
    }
}
