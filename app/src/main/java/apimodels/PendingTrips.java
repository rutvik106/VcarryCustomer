package apimodels;

import java.util.List;

/**
 * Created by rutvikmehta on 15/03/18.
 */

public class PendingTrips {
    private List<Integer> pending_customer_trips;

    public List<Integer> getPending_trips() {
        return pending_customer_trips;
    }

    public void setPending_trips(List<Integer> pending_trips) {
        this.pending_customer_trips = pending_trips;
    }
}
