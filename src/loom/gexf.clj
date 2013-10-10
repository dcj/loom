(ns ^{:doc "Adaptation of clj-gexf, https://github.com/mihi-tr/clj-gexf.git"}
  loom.gexf
  (:require [loom.graph :refer :all]
            [loom.attr :refer :all]
            [loom.label :refer :all]

            [hiccup.core :refer :all]

            [plumbing.core :refer :all]
            )
  )

(defn make-node
  "TODO: add attributes"
  [g id]
  [:node (-> {:id (str id)}
             (assoc-when :label (attr g id :label)))]) ;; TODO ensure attr value is a string

(defn make-nodes
  [g]
  [:nodes (for [node (nodes g)]
            (make-node g node))])

(defn make-edge-id
  [source target]
  (str source 
       "->"
       target))

(defn make-edge
  "TODO: add attributes"
  [g [source target]]
  [:edge (-> {:id (make-edge-id source target)
              :source (str source) 
              :target (str target)}
             (assoc-when :label (attr g source target :label)))])

(defn make-edges
  "get all edges"
  [g]
  [:edges (for [edge (edges g)]
            (make-edge g edge))])

(defn make-graph
  ([g]
     (make-graph g {:mode "static" 
                    :defaultedgetype (if (directed? g)
                                       "directed"
                                       "undirected")}))
  ([g opts]
     [:graph opts
      (make-nodes g)
      (make-edges g)]))

(defn export
  "write the gexf xml"
  [g & {:as opts}]
  (str "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
       (html [:gexf {:xmlns "http://www.gexf.net/1.2draft"
                     :xmlns:xsi "http://www.w3.org/2001/XMLSchema-instance"
                     :xsi:schemaLocation "http://www.gexf.net/1.2draft http://www.gexf.net/1.2draft/gexf.xsd"
                     :version "1.2"}
              (if opts
                (make-graph g opts)
                (make-graph g))
              ])))
  
