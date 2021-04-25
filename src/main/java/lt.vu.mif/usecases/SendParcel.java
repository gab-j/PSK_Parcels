package lt.vu.mif.usecases;

import lombok.Getter;
import lombok.Setter;
import lt.vu.mif.entities.Parcel;
import lt.vu.mif.persistence.ParcelsDAO;

import javax.enterprise.context.Dependent;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Model;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

@Model
@Getter
@Setter
@SessionScoped
public class SendParcel implements Serializable {

    @Inject
    private ParcelsDAO parcelsDAO;

    private Parcel parcelToSend = new Parcel();

    private List<String> selectedOptions = new ArrayList<>();

    public String goToPayment(){
        return "send3.xhtml";
    }

    public String goToParcelParameters(){
        return "send2.xhtml";
    }

    public String goToRecipientParameters(){
        return "send1.xhtml";
    }

    public String goToHomePage(){
        return "index.xhtml";
    }

    private final Map<String, BigDecimal> priceOptions = new HashMap<String, BigDecimal>() {{
        put("fragile", new BigDecimal("5"));
        put("signDocument", new BigDecimal("3"));
        put("donateToChildren", new BigDecimal("2"));
        put("sustainable", new BigDecimal("30"));
    }};

    public void calcPrice() {
        BigDecimal price = new BigDecimal("0.00");

        if (parcelToSend.getLength() > 50 || parcelToSend.getHeight() > 50 || parcelToSend.getWidth() > 50) {
            price = price.add(new BigDecimal("10"));
        }

        for (String option : selectedOptions) {
            price = price.add(priceOptions.get(option));
        }

        parcelToSend.setPrice(price);
        FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("parcelInfoForm:currentPrice");
    }

    private final Map<Integer, String> payOptions = new HashMap<Integer, String>() {{
        put(Parcel.PAY_OPTION_BANK, "Internetine bankininkyste");
        put(Parcel.PAY_OPTION_CASH, "Grynaisiais (paėmimo metu)");
        put(Parcel.PAY_OPTION_PAYPAL, "PayPal");
    }};

    public List<Map.Entry<Integer, String>> getPayOptions(){
        return new ArrayList<>(payOptions.entrySet());
    }

    public List<Map.Entry<String, BigDecimal>> getPriceOptions(){
        return new ArrayList<>(priceOptions.entrySet());
    }

    private final Map<String, String> priceOptionsNames = new HashMap<String, String>() {{
        put("fragile", "Dūžtanti siunta, (+5€ * masė)");
        put("signDocument", "Priimant būtina pasirašyti dokumentą (+3€)");
        put("donateToChildren", "Paremti globos namuose esančius vaikus (+2€)");
        put("sustainable", "Siųsti gamtą tausojančiu būdu (siuntą siųs kurjeris su dviračiu) (+30€)");
    }};

    @Transactional
    public String commitSend(){
        parcelsDAO.persist(parcelToSend);
        parcelToSend = new Parcel();
        System.out.println(parcelToSend.toString());
        return "index.xhtml";
    }
}
