(ns ring.middleware.accept-param)

(def default-format :html)

(def formats {:json  "^application/json"
              :html  "^text/html"
              :xml   "^text/xml"
              :csv   "^text/csv"
              :plain "^text/plain"})

(defn accept-format?
  "Returns true if value match the given pattern, false or nil otherwise."
  { :added "0.0.1" }
  [value pattern]
  (when-not (nil? value)
    (not
      (empty?
        (re-seq (re-pattern pattern) value)))))

(defn match-accept
  "Returns matching entry from formats map given accept-header value."
  { :added "0.0.1" }
  [accept-header]
  (select-keys formats
    (for [[format-keyword format-pattern]
          formats
          :when (accept-format? accept-header format-pattern)]
      format-keyword)))

(defn extract-first-format
  "Returns first keyword as accepted format if exists, default value otherwise.

  At this point, accept-map should be empty or contain one entry. If accept-map cointains more
  than one entry, keys are sorted by ascending value and the first one is returned."
  { :added "0.0.1" }
  [accept-map]
  (if (empty? accept-map)
    (name default-format)
    (when-first [first-format (keys (into (sorted-map) accept-map))]
      (name first-format))))

(defn detect-accept
  "Builds the map based on Accept request-header field analysis."
  { :added "0.0.1" }
  [accept-header accept-param]
  (let [accept-map    (match-accept accept-header)
        accept-header (extract-first-format accept-map)
        accept        (if (nil? accept-param) accept-header accept-param)]
    {:accept accept}))

(defn wrap-accept-param
  "Augments :params according to the specified Accept request-header field."
  { :added "0.0.1" }
  [handler]
  (fn [req]
    (let [#^String accept-header (get (:headers req) "accept")
                   accept-param  (get (:params req) :format)
                   accept        (detect-accept accept-header accept-param)
                   req*          (assoc req :params (merge accept (:params req)))]
      (handler req*))))
