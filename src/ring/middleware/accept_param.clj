(ns ring.middleware.accept-param)

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
