(ns ring.middleware.accept-param)

(def default-format :html)

(def formats {:json "^application/json"
	          :html "^text/html"
	          :xml "^text/xml"})

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
	"Builds the  based on Accept request-header field analysis."
	{ :added "0.0.1" }
	[accept-header]
	{"accept" (extract-first-format (match-accept accept-header))})

(defn wrap-accept-param
	"Augments :params according to the specified Accept request-header field."
	{ :added "0.0.1" }
	[handler]
	(fn [req]
		(let [#^String accept-header (get (:headers req) "accept")
			           accept        (detect-accept accept-header)
			           req*          (assoc req :params (merge accept (:params req)))]
			(handler req*))))
