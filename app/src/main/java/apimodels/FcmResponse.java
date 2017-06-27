package apimodels;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by rutvik on 6/27/2017 at 1:44 PM.
 */

public class FcmResponse
{

    /**
     * multicast_id : 9082170319080776633
     * success : 0
     * failure : 1
     * canonical_ids : 0
     * results : [{"error":"NotRegistered"}]
     */

    @SerializedName("multicast_id")
    private long multicastId;
    @SerializedName("success")
    private int success;
    @SerializedName("failure")
    private int failure;
    @SerializedName("canonical_ids")
    private int canonicalIds;
    @SerializedName("results")
    private List<ResultsBean> results;

    public long getMulticastId()
    {
        return multicastId;
    }

    public void setMulticastId(long multicastId)
    {
        this.multicastId = multicastId;
    }

    public int getSuccess()
    {
        return success;
    }

    public void setSuccess(int success)
    {
        this.success = success;
    }

    public int getFailure()
    {
        return failure;
    }

    public void setFailure(int failure)
    {
        this.failure = failure;
    }

    public int getCanonicalIds()
    {
        return canonicalIds;
    }

    public void setCanonicalIds(int canonicalIds)
    {
        this.canonicalIds = canonicalIds;
    }

    public List<ResultsBean> getResults()
    {
        return results;
    }

    public void setResults(List<ResultsBean> results)
    {
        this.results = results;
    }

    public static class ResultsBean
    {
        /**
         * error : NotRegistered
         */

        @SerializedName("error")
        private String error;

        public String getError()
        {
            return error;
        }

        public void setError(String error)
        {
            this.error = error;
        }
    }
}
