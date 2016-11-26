package firebase;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by rutvik on 11/20/2016 at 5:02 PM.
 */

public class TripOperations
{

    public static void bookTrip(final String from, final String to, final TripOperationListener tripOperationListener)
    {

        final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        dbRef.getRoot();

        Random r = new Random();
        int i1 = r.nextInt(100 - 1) + 1;

        final String requestId = i1 + "";

        Map data = new HashMap<>();
        data.put("from", from);
        data.put("to", to);
        data.put("id", requestId);

        dbRef.child("request").child(requestId).updateChildren(data, new DatabaseReference.CompletionListener()
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
