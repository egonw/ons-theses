# Theses to Wikidata

Scripts to extract chemistry from theses metadata and create QuickStatements for Wikidata.

The scripts are written in Groovy and will "grab" a few dependencies from Maven Central
on the fly, including Bacting plugins.

```shell
dos2unix ThesesToCompoundsMapping.csv
groovy extractSMILES.groovy | tee theses.smi
```

The output of this extraction, `thesis.smi`, is used as input for the
`Wikidata/createWDitemsFromSMILES.groovy` script available from
[https://github.com/egonw/ons-wikidata](https://github.com/egonw/ons-wikidata).
