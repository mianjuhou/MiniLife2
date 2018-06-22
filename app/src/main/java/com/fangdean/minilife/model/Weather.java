package com.fangdean.minilife.model;

import java.util.List;

public class Weather {

    /**
     * data : {"yesterday":{"date":"12日星期二","high":"高温 32℃","fx":"东南风","low":"低温 19℃","fl":"<![CDATA[3-4级]]>","type":"雷阵雨"},"city":"北京","aqi":"49","forecast":[{"date":"13日星期三","high":"高温 28℃","fengli":"<![CDATA[<3级]]>","low":"低温 18℃","fengxiang":"东北风","type":"雷阵雨"},{"date":"14日星期四","high":"高温 31℃","fengli":"<![CDATA[<3级]]>","low":"低温 20℃","fengxiang":"南风","type":"多云"},{"date":"15日星期五","high":"高温 33℃","fengli":"<![CDATA[<3级]]>","low":"低温 22℃","fengxiang":"南风","type":"多云"},{"date":"16日星期六","high":"高温 34℃","fengli":"<![CDATA[<3级]]>","low":"低温 21℃","fengxiang":"南风","type":"多云"},{"date":"17日星期天","high":"高温 28℃","fengli":"<![CDATA[<3级]]>","low":"低温 20℃","fengxiang":"东北风","type":"雷阵雨"}],"ganmao":"各项气象条件适宜，无明显降温过程，发生感冒机率较低。","wendu":"21"}
     * status : 1000
     * desc : OK
     */

    private DataBean data;
    private int status;
    private String desc;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static class DataBean {
        /**
         * yesterday : {"date":"12日星期二","high":"高温 32℃","fx":"东南风","low":"低温 19℃","fl":"<![CDATA[3-4级]]>","type":"雷阵雨"}
         * city : 北京
         * aqi : 49
         * forecast : [{"date":"13日星期三","high":"高温 28℃","fengli":"<![CDATA[<3级]]>","low":"低温 18℃","fengxiang":"东北风","type":"雷阵雨"},{"date":"14日星期四","high":"高温 31℃","fengli":"<![CDATA[<3级]]>","low":"低温 20℃","fengxiang":"南风","type":"多云"},{"date":"15日星期五","high":"高温 33℃","fengli":"<![CDATA[<3级]]>","low":"低温 22℃","fengxiang":"南风","type":"多云"},{"date":"16日星期六","high":"高温 34℃","fengli":"<![CDATA[<3级]]>","low":"低温 21℃","fengxiang":"南风","type":"多云"},{"date":"17日星期天","high":"高温 28℃","fengli":"<![CDATA[<3级]]>","low":"低温 20℃","fengxiang":"东北风","type":"雷阵雨"}]
         * ganmao : 各项气象条件适宜，无明显降温过程，发生感冒机率较低。
         * wendu : 21
         */

        private Yesterday yesterday;
        private String city;
        private String aqi;
        private String ganmao;
        private String wendu;
        private List<Forecast> forecast;

        public Yesterday getYesterday() {
            return yesterday;
        }

        public void setYesterday(Yesterday yesterday) {
            this.yesterday = yesterday;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getAqi() {
            return aqi;
        }

        public void setAqi(String aqi) {
            this.aqi = aqi;
        }

        public String getGanmao() {
            return ganmao;
        }

        public void setGanmao(String ganmao) {
            this.ganmao = ganmao;
        }

        public String getWendu() {
            return wendu;
        }

        public void setWendu(String wendu) {
            this.wendu = wendu;
        }

        public List<Forecast> getForecast() {
            return forecast;
        }

        public void setForecast(List<Forecast> forecast) {
            this.forecast = forecast;
        }

        public static class Yesterday {
            /**
             * date : 12日星期二
             * high : 高温 32℃
             * fx : 东南风
             * low : 低温 19℃
             * fl : <![CDATA[3-4级]]>
             * type : 雷阵雨
             */

            private String date;
            private String high;
            private String fx;
            private String low;
            private String fl;
            private String type;

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getHigh() {
                return high;
            }

            public void setHigh(String high) {
                this.high = high;
            }

            public String getFx() {
                return fx;
            }

            public void setFx(String fx) {
                this.fx = fx;
            }

            public String getLow() {
                return low;
            }

            public void setLow(String low) {
                this.low = low;
            }

            public String getFl() {
                return fl;
            }

            public void setFl(String fl) {
                this.fl = fl;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }
        }

        public static class Forecast {
            /**
             * date : 13日星期三
             * high : 高温 28℃
             * fengli : <![CDATA[<3级]]>
             * low : 低温 18℃
             * fengxiang : 东北风
             * type : 雷阵雨
             */

            private String date;
            private String high;
            private String fengli;
            private String low;
            private String fengxiang;
            private String type;

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getHigh() {
                return high;
            }

            public void setHigh(String high) {
                this.high = high;
            }

            public String getFengli() {
                return fengli;
            }

            public void setFengli(String fengli) {
                this.fengli = fengli;
            }

            public String getLow() {
                return low;
            }

            public void setLow(String low) {
                this.low = low;
            }

            public String getFengxiang() {
                return fengxiang;
            }

            public void setFengxiang(String fengxiang) {
                this.fengxiang = fengxiang;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }
        }
    }
}
