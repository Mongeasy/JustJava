package com.example.android.justjava;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // Global variable
    private static int mQuantity = 1;

    /**
     * This method is called when the minus button is clicked.
     */
    public void decrement(View view) {
        // Set min number of coffee orders to be 1 or more
        if (mQuantity == 1) {
            Toast.makeText(this,
                    "Cannot order less than 1 cup of coffee.", Toast.LENGTH_SHORT).show();
            // Exit if statement method
            return;
        }
        // decrement by 1 coffee cup each time
        mQuantity -= 1;
        // show number of coffees ordered
        displayQuantity(mQuantity);
    }

    /**
     * This method is called when the add button is clicked.
     */
    public void increment(View view) {
        // Set max number of coffee orders to be 100 or less
        if (mQuantity == 100) {
            Toast.makeText(this, "You cannot order more than 100 cups of coffee.",
                    Toast.LENGTH_SHORT).show();
            // Exit if statement method
            return;
        }
        // increment by 1 coffee cup each time
        mQuantity += 1;
        // show number of coffees ordered
        displayQuantity(mQuantity);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        // Take in user's name
        EditText nameField = findViewById(R.id.name);
        Editable name = nameField.getText();

        // Figure out whether user wants whipped cream topping
        CheckBox whippedCreamCheckBox = findViewById(R.id.whipped_cream_checkbox);
        boolean hasWhippedCream = whippedCreamCheckBox.isChecked();

        // Figure out whether user wants chocolate topping
        CheckBox chocolateCheckbox = findViewById(R.id.chocolate_checkbox);
        boolean hasChocolate = chocolateCheckbox.isChecked();

        int price = calculatePrice(hasWhippedCream, hasChocolate);

        String priceMessage = createOrderSummary(price, hasWhippedCream, hasChocolate, name);

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:")); // only email apps should handle this
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "JustJava Coffee Order for " + name);
        emailIntent.putExtra(Intent.EXTRA_TEXT, priceMessage);
        if (emailIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(emailIntent);
        }
    }

    /**
     * @param price           of the order
     * @param addWhippedCream determines whether or not user wants whipped cream topping
     * @return String text summary
     */
    private String createOrderSummary(int price, boolean addWhippedCream,
                                      boolean addChocolate, Editable name) {
        return getString(R.string.name) + ":" + "\t" + name
                + "\n" + getString(R.string.whipped_cream) + ":" + "\t" + addWhippedCream
                + "\n" + getString(R.string.chocolate) + ":" + "\t" + addChocolate
                + "\n" + getString(R.string.quantity) + ":" + "\t" + mQuantity
                + "\n" + getString(R.string.total) + ":" + "\t" + "R" + price
                + "\n" + getString(R.string.thanks);
    }

    /**
     * Calculates the price of the order.
     *
     * @param hasWhippedCream topping on coffee
     * @param hasChocolate    topping on coffee
     * @return cups of coffee multiplied by price per cup of coffee
     */
    private int calculatePrice(boolean hasWhippedCream, boolean hasChocolate) {
        // price of a cup of coffee
        int pricePerCup = 5;

        // Add R1 if user wants whipped cream topping
        if (hasWhippedCream) {
            pricePerCup += 1;
        }

        // Add R2 if user wants chocolate topping
        if (hasChocolate) {
            pricePerCup += 2;
        }

        // Calculate total price
        return mQuantity * pricePerCup;
    }

    /**
     * This method displays the given mQuantity value on the screen.
     */
    private void displayQuantity(int numberOfCoffees) {
        TextView quantityTextView = findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + numberOfCoffees);
    }
}