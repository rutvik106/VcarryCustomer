package firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rutvik on 11/20/2016 at 5:02 PM.
 */

public class TripOperations
{

    public static void bookTrip(final TripOperationListener tripOperationListener)
    {

        final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        dbRef.getRoot();

        final String requestId = Calendar.getInstance().getTimeInMillis() + "";

        Map data = new HashMap<>();
        data.put("name", FirebaseAuth.getInstance().getCurrentUser().getEmail());
        dbRef.child("customer-request").child(requestId).updateChildren(data, new DatabaseReference.CompletionListener()
        {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference)
            {
                if (databaseError == null)
                {
                    tripOperationListener.tripBookedSuccessfully();
                } else
                {
                    tripOperationListener.failedToBookTrip(databaseError);
                }
            }
        });
    }

    public interface TripOperationListener
    {

        void tripBookedSuccessfully();

        void failedToBookTrip(DatabaseError databaseError);

    }

}
