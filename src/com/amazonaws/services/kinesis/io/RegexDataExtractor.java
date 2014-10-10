package com.amazonaws.services.kinesis.io;

import java.util.List;

import com.amazonaws.services.kinesis.io.serialiser.CsvSerialiser;
import com.amazonaws.services.kinesis.io.serialiser.RegexSerialiser;

/**
 * IDataExtractor implementation which allows for extraction of data from
 * Streams formatted as Character Separated Values. Also optionally allows for
 * regular expression based filtering of the stream prior to aggregation.
 */
public class RegexDataExtractor extends StringDataExtractor<RegexDataExtractor> implements
        IDataExtractor {
    private String regex;

    private RegexSerialiser serialiser;

    /**
     * Create a new data extractor using the indicated index for the label value
     * to be aggregated on, and the regular expression used for data extraction
     * 
     * @param labelIndex Index (base 0) of where in the CSV stream the label
     *        value occurs
     * @param delimiter The character delimiter separating items in the stream
     *        data.
     */
    public RegexDataExtractor(String regex, List<Integer> labelIndicies) {
        this(regex, labelIndicies, null, -1, null, null);
    }

    public RegexDataExtractor(String regex, List<Integer> labelIndicies, int dateValueIndex) {
        this(regex, labelIndicies, null, dateValueIndex, null, null);
    }

    public RegexDataExtractor(String regex, List<Integer> labelIndicies,
            String labelAttributeAlias, int dateValueIndex, String dateAttributeAlias,
            RegexSerialiser serialiser) {
        this.regex = regex;
        super.labelIndicies = labelIndicies;
        super.labelAttributeAlias = labelAttributeAlias;
        super.dateAttributeAlias = dateAttributeAlias;

        if (dateValueIndex != -1)
            super.dateValueIndex = dateValueIndex;
        if (serialiser != null) {
            super.serialiser = serialiser;
        } else {
            super.serialiser = new RegexSerialiser(regex);
        }
    }

    /**
     * Add a non default item terminator. The default is "\n"
     * 
     * @param lineTerminator The characters used for delimiting lines of text
     * @return
     */
    public RegexDataExtractor withItemTerminator(String lineTerminator) {
        if (lineTerminator != null) {
            this.serialiser.withItemTerminator(lineTerminator);
            super.serialiser = this.serialiser;
        }
        return this;
    }

    /**
     * Add a custom configured serialiser
     * 
     * @param serialiser
     * @return
     */
    public RegexDataExtractor withSerialiser(CsvSerialiser serialiser) {
        super.serialiser = serialiser;
        return this;
    }

    /**
     * Builder method for adding a index to the extraction configuration which
     * indicates where the date item to be used for aggregation can be found.
     * 
     * @param dateValueIndex The index value (base 0) in the CSV stream which
     *        contains the date value.
     * @return
     */
    public RegexDataExtractor withDateValueIndex(Integer dateValueIndex) {
        if (dateValueIndex != null) {
            this.dateValueIndex = dateValueIndex;
        }
        return this;
    }

    @Override
    public IDataExtractor copy() throws Exception {
        RegexDataExtractor dataExtractor = new RegexDataExtractor(this.regex, this.labelIndicies,
                super.labelAttributeAlias, this.dateValueIndex, super.dateAttributeAlias,
                this.serialiser).withSummaryIndicies(this.getOriginalSummaryExpressions());

        return dataExtractor;
    }
}