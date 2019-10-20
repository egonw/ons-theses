@Grab("com.xlson.groovycsv:groovycsv:1.1")
@Grab(group='io.github.egonw.bacting', module='managers-chemspider', version='0.0.11-SNAPSHOT')

import static com.xlson.groovycsv.CsvParser.parseCsv

workspace = "."
chemspider = new net.bioclipse.managers.ChemspiderManager(workspace);

// ChemSpiderAuthor,ChemSpiderTitle,Year,ChemSpiderThesisID,ChemSpiderThesisURL,ChemSpiderCompoundID,ChemSpiderID,ChemSpiderCompoundURL,IdentifiersOrgCompoundURL
for (line in parseCsv(new FileReader('ThesesToCompoundsMapping.csv'))) {
  try {
    csid = line.ChemSpiderID
    mol = chemspider.download(new Integer(csid))
    println "${mol.toSMILES()}\t$csid"
  } catch (Exception exception) {
    message = exception.message.replace('\n',"; ")
    println "# $message"
  }
}


