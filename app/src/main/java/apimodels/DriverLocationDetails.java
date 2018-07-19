package apimodels;

import android.support.v4.util.TimeUtils;
import android.text.format.DateUtils;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

public class DriverLocationDetails {


    /**
     * response : {"error":{"error_code":0,"error_msg":"","msg":"location updates successfully"},"driver_details":{"id":"2","name":"Rutvik Mehta","recent_location":"26.117605,91.7983317","location_time":"2018-07-19 16:21:44","mobile":"9409210488","image_url":"https://lh3.googleusercontent.com/-Wlkp-_tMv-Y/AAAAAAAAAAI/AAAAAAAAAAA/NbvcGT31kjM/s120-c/photo.jpg"}}
     */

    private ResponseBean response;

    public ResponseBean getResponse() {
        return response;
    }

    public void setResponse(ResponseBean response) {
        this.response = response;
    }

    public static class ResponseBean {
        /**
         * error : {"error_code":0,"error_msg":"","msg":"location updates successfully"}
         * driver_details : {"id":"2","name":"Rutvik Mehta","recent_location":"26.117605,91.7983317","location_time":"2018-07-19 16:21:44","mobile":"9409210488","image_url":"https://lh3.googleusercontent.com/-Wlkp-_tMv-Y/AAAAAAAAAAI/AAAAAAAAAAA/NbvcGT31kjM/s120-c/photo.jpg"}
         */

        private ErrorBean error;
        private DriverDetailsBean driver_details;

        public ErrorBean getError() {
            return error;
        }

        public void setError(ErrorBean error) {
            this.error = error;
        }

        public DriverDetailsBean getDriver_details() {
            return driver_details;
        }

        public void setDriver_details(DriverDetailsBean driver_details) {
            this.driver_details = driver_details;
        }

        public static class ErrorBean {
            /**
             * error_code : 0
             * error_msg :
             * msg : location updates successfully
             */

            private int error_code;
            private String error_msg;
            private String msg;

            public int getError_code() {
                return error_code;
            }

            public void setError_code(int error_code) {
                this.error_code = error_code;
            }

            public String getError_msg() {
                return error_msg;
            }

            public void setError_msg(String error_msg) {
                this.error_msg = error_msg;
            }

            public String getMsg() {
                return msg;
            }

            public void setMsg(String msg) {
                this.msg = msg;
            }
        }

        public static class DriverDetailsBean {
            /**
             * id : 2
             * name : Rutvik Mehta
             * recent_location : 26.117605,91.7983317
             * location_time : 2018-07-19 16:21:44
             * mobile : 9409210488
             * image_url : https://lh3.googleusercontent.com/-Wlkp-_tMv-Y/AAAAAAAAAAI/AAAAAAAAAAA/NbvcGT31kjM/s120-c/photo.jpg
             */

            private int id;
            private String name;
            private String recent_location;
            private Date location_time;
            private String mobile;
            private String image_url;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getRecent_location() {
                if (recent_location == null) {
                    return null;
                } else if (recent_location.isEmpty()) {
                    return null;
                }
                return recent_location;
            }

            public void setRecent_location(String recent_location) {
                this.recent_location = recent_location;
            }

            public Date getLocation_time() {
                return location_time;
            }

            public String getLocation_time_ago() {
                long now = System.currentTimeMillis();
                CharSequence ago =
                        DateUtils.getRelativeTimeSpanString(location_time.getTime(), now, DateUtils.MINUTE_IN_MILLIS);
                if (ago.charAt(0) == '0') {
                    return "Just Now";
                }
                return ago.toString();
            }

            public void setLocation_time(Date location_time) {
                this.location_time = location_time;
            }

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }

            public String getImage_url() {
                return image_url;
            }

            public void setImage_url(String image_url) {
                this.image_url = image_url;
            }

            public LatLng getRecentLatLng() {
                final String[] latLng = recent_location.split(",");
                final Double lat = Double.valueOf(latLng[0]);
                final Double lng = Double.valueOf(latLng[1]);
                return new LatLng(lat, lng);
            }
        }
    }
}
