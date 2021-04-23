package lt.vu.mif.usecases;

import lombok.Getter;
import lombok.Setter;
import lt.vu.mif.entities.Parcel;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Model;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Model
@Getter
@Setter
@SessionScoped
public class SendParcel implements Serializable {

    private Parcel parcelToSend = new Parcel();

    private List<String> selectedOptions;

    public String goToPayment(){
        return "send3.xhtml";
    }

    public void calcPrice() {
        BigDecimal price = new BigDecimal("0.00");

        if (parcelToSend.getLength() > 50 || parcelToSend.getHeight() > 50 || parcelToSend.getWidth() > 50) {
            price = price.add(new BigDecimal("10"));
        }

        if(selectedOptions != null) {
            for (String option : selectedOptions) {
                switch (option) {
                    case "fragile":
                        price = price.add(new BigDecimal("5"));
                        break;
                    case "signDocument":
                        price = price.add(new BigDecimal("3"));
                        break;
                    case "donateToChildren":
                        price = price.add(new BigDecimal("2"));
                        break;
                    case "sustainable":
                        price = price.add(new BigDecimal("30"));
                        break;
                }
            }
        }

        parcelToSend.setPrice(price);
        FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("parcelInfoForm:currentPrice");
    }
}