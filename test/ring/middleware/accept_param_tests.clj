(ns ring.middleware.accept-param-tests
	(:use clojure.test)
	(:require [ring.middleware.accept-param :as mdw]))

; test accept-format?
(deftest is-json
	(is (mdw/accept-format? "application/json" "^application/json")))

(deftest xml-is-not-json
	(is (not (mdw/accept-format? "text/xml" "^application/json"))))

(deftest empty-string-is-not-json
	(is (not (mdw/accept-format? "" "^application/json"))))

(deftest nil-is-not-json
	(is (not (mdw/accept-format? nil "^application/json"))))

; test match-accept
(deftest match-nil
	(is (= {} (mdw/match-accept nil))))

(deftest match-empty-string
	(is (= {} (mdw/match-accept ""))))

(deftest match-unsupported-format
	(is (= {} (mdw/match-accept "audio/mpeg"))))

(deftest match-json-format
	(is (= {:json "^application/json"} (mdw/match-accept "application/json"))))

(deftest match-html-format
	(is (= {:html "^text/html"} (mdw/match-accept "text/html;level=1"))))

(deftest match-xml-format
	(is (= {:xml "^text/xml"} (mdw/match-accept "text/xml;q=0.8"))))

; test extract-first-format
(deftest extract-from-nil
	(is (= "html" (mdw/extract-first-format nil))))

(deftest extract-from-emtpy-map
	(is (= "html" (mdw/extract-first-format {}))))

(deftest extract-from-one-entry-map
	(is (= "xml" (mdw/extract-first-format {:xml "^text/xml"}))))

(deftest extract-from-multiple-entries-map
	(is (= "html" (mdw/extract-first-format {:html "^text/html" :json "^application/json" :xml "^text/xml"}))))

(deftest extract-from-multiple-entries-map-inversed
	(is (= "html" (mdw/extract-first-format {:xml "^text/xml" :json "^application/json" :html "^text/html"}))))

; test augments-with-accept-json
(def echo-wrapper
	(mdw/wrap-accept-param identity))

(deftest augments-with-accept-json
	(let [req {:accept "application/json"
			   :params {"id" "4f4ceffde4b0e75979becd25"}}
		  resp (echo-wrapper req)]
		(is (= {"id" "4f4ceffde4b0e75979becd25" "accept" "json"} (:params resp)))))

(deftest augments-with-accept-json-and-level
	(let [req {:accept "application/json;level=1"
			   :params {"id" "4f4ceffde4b0e75979becd25"}}
		  resp (echo-wrapper req)]
		(is (= {"id" "4f4ceffde4b0e75979becd25" "accept" "json"} (:params resp)))))

(deftest augments-with-accept-json-and-quality
	(let [req {:accept "application/json;q=0.8"
			   :params {"id" "4f4ceffde4b0e75979becd25"}}
		  resp (echo-wrapper req)]
		(is (= {"id" "4f4ceffde4b0e75979becd25" "accept" "json"} (:params resp)))))

(deftest augments-with-accept-json-and-star
	(let [req {:accept "application/json,*/*"
			   :params {"id" "4f4ceffde4b0e75979becd25"}}
		  resp (echo-wrapper req)]
		(is (= {"id" "4f4ceffde4b0e75979becd25" "accept" "json"} (:params resp)))))

(deftest augments-with-accept-json-first
	(let [req {:accept "application/json,text/xml,text/html"
			   :params {"id" "4f4ceffde4b0e75979becd25"}}
		  resp (echo-wrapper req)]
		(is (= {"id" "4f4ceffde4b0e75979becd25" "accept" "json"} (:params resp)))))

(deftest augments-with-accept-xml
	(let [req {:accept "text/xml"
			   :params {"id" "4f4ceffde4b0e75979becd25"}}
		  resp (echo-wrapper req)]
		(is (= {"id" "4f4ceffde4b0e75979becd25" "accept" "xml"} (:params resp)))))

(deftest augments-with-accept-html
	(let [req {:accept "text/html"
			   :params {"id" "4f4ceffde4b0e75979becd25"}}
		  resp (echo-wrapper req)]
		(is (= {"id" "4f4ceffde4b0e75979becd25" "accept" "html"} (:params resp)))))

(deftest augments-with-accept-html-if-no-accept-request-header
	(let [req {:params {"id" "4f4ceffde4b0e75979becd25"}}
		  resp (echo-wrapper req)]
		(is (= {"id" "4f4ceffde4b0e75979becd25" "accept" "html"} (:params resp)))))

(deftest augments-with-accept-html-if-unrecognized-accept-request-header
	(let [req {:params {"id" "4f4ceffde4b0e75979becd25"}}
		  resp (echo-wrapper req)]
		(is (= {"id" "4f4ceffde4b0e75979becd25" "accept" "html"} (:params resp)))))
