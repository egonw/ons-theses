@Grab("com.xlson.groovycsv:groovycsv:1.1")
@Grab(group='io.github.egonw.bacting', module='managers-chemspider', version='0.0.11-SNAPSHOT')
@Grab(group='io.github.egonw.bacting', module='managers-rdf', version='0.0.11-SNAPSHOT')

import static com.xlson.groovycsv.CsvParser.parseCsv
import static java.util.stream.Collectors.joining

workspace = "."
chemspider = new net.bioclipse.managers.ChemspiderManager(workspace);
rdf = new net.bioclipse.managers.RDFManager(workspace);

// First, extract all ChemSpider IDs from the CSV file.

// ChemSpiderAuthor,ChemSpiderTitle,Year,ChemSpiderThesisID,ChemSpiderThesisURL,ChemSpiderCompoundID,ChemSpiderID,ChemSpiderCompoundURL,IdentifiersOrgCompoundURL
csids = new java.util.ArrayList<String>()
for (line in parseCsv(new FileReader('ThesesToCompoundsMapping.csv'))) {
  csids.add(line.ChemSpiderID)
}

// Convert the Java List to a space-delimited list of quoted identifiers, which
//   we can later insert into the SPARQL query
csidsValues = csids.collect{it -> "\"$it\""}.stream().collect(joining(" "))

// Create the SPARQL to find all Wikidata items with ChemSpider IDs from the CSV file
sparql = """
PREFIX wdt: <http://www.wikidata.org/prop/direct/>
SELECT ?qid ?csid WHERE {
  VALUES ?csid { $csidsValues }
  ?qid wdt:P661 ?csid .
}
"""

// Run the query and put the results in a Java Map
results = rdf.sparqlRemote(
  "https://query.wikidata.org/sparql", sparql
)
csidHits = new java.util.HashMap();
for (i in 1..results.rowCount) {
  csidHits.put(
    results.get(i, "csid"),
    results.get(i, "qid").substring(31)
  )
}

// ChemSpiderAuthor,ChemSpiderTitle,Year,ChemSpiderThesisID,ChemSpiderThesisURL,ChemSpiderCompoundID,ChemSpiderID,ChemSpiderCompoundURL,IdentifiersOrgCompoundURL
for (line in parseCsv(new FileReader('ThesesToCompoundsMapping.csv'))) {
  try {
    csid = line.ChemSpiderID
    if (!csidHits.containsKey(csid)) {
      mol = chemspider.download(new Integer(csid))
      println "${mol.toSMILES()}\t$csid"
    } else {
      println "# ChemSpider ID $csid is already in Wikidata"
    }
  } catch (Exception exception) {
    message = exception.message.replace('\n',"; ")
    println "# $message"
  }
}


