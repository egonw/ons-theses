# Theses to Wikidata

Scripts to extract chemistry from theses metadata and create QuickStatements for Wikidata.

The scripts are written in Groovy and will "grab" a few dependencies from Maven Central
on the fly, including Bacting plugins.

The file may require first a conversion of DOS newlines to UNIX newlines:

```shell
dos2unix ThesesToCompoundsMapping.csv
```

## Which ChemSpider IDs are in Wikidata

The first step is to see which ChemSpider IDs are already found in Wikidata:

```shell
groovy reportExistingHits.groovy
```

## Do structural searches

```shell
groovy extractSMILES.groovy | tee theses.smi
```

The output of this extraction, `thesis.smi`, is used as input for the
`Wikidata/createWDitemsFromSMILES.groovy` script available from
[https://github.com/egonw/ons-wikidata](https://github.com/egonw/ons-wikidata).
