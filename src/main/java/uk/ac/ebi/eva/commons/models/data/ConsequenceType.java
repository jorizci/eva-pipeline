/*
 * Copyright 2015-2017 EMBL - European Bioinformatics Institute
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.ac.ebi.eva.commons.models.data;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import uk.ac.ebi.eva.commons.models.converters.data.AnnotationFieldNames;

import java.util.List;

/**
 * org.opencb.biodata.models.variant.annotation.ConsequenceType
 */
@Document
public class ConsequenceType {

    @Field(value = AnnotationFieldNames.GENE_NAME_FIELD)
    private String geneName;

    @Field(value = AnnotationFieldNames.ENSEMBL_GENE_ID_FIELD)
    private String ensemblGeneId;

    @Field(value = AnnotationFieldNames.ENSEMBL_TRANSCRIPT_ID_FIELD)
    private String ensemblTranscriptId;

    @Field(value = AnnotationFieldNames.STRAND_FIELD)
    private String strand;

    @Field(value = AnnotationFieldNames.BIOTYPE_FIELD)
    private String biotype;

    @Field(value = AnnotationFieldNames.C_DNA_POSITION_FIELD)
    private Integer cDnaPosition;

    @Field(value = AnnotationFieldNames.CDS_POSITION_FIELD)
    private Integer cdsPosition;

    @Field(value = AnnotationFieldNames.AA_POSITION_FIELD)
    private Integer aaPosition;

    @Field(value = AnnotationFieldNames.AA_CHANGE_FIELD)
    private String aaChange;

    @Field(value = AnnotationFieldNames.CODON_FIELD)
    private String codon;

    @Field(value = AnnotationFieldNames.SIFT_FIELD)
    private Score sifts;

    @Field(value = AnnotationFieldNames.POLYPHEN_FIELD)
    private Score polyphen;

    @Field(value = AnnotationFieldNames.SO_ACCESSION_FIELD)
    private List<Integer> soAccessions;

    @Field(value = AnnotationFieldNames.RELATIVE_POS_FIELD)
    private Integer relativePosition;

    public ConsequenceType() {
    }

    public void setEnsemblTranscriptId(String ensemblTranscriptId) {
        this.ensemblTranscriptId = ensemblTranscriptId;
    }

    public void setGeneName(String geneName) {
        this.geneName = geneName;
    }

    public void setEnsemblGeneId(String ensemblGeneId) {
        this.ensemblGeneId = ensemblGeneId;
    }

    public void setRelativePosition(Integer relativePosition) {
        this.relativePosition = relativePosition;
    }

    public void setCodon(String codon) {
        this.codon = codon;
    }

    public void setStrand(String strand) {
        this.strand = strand;
    }

    public void setBiotype(String biotype) {
        this.biotype = biotype;
    }

    public void setcDnaPosition(Integer cDnaPosition) {
        this.cDnaPosition = cDnaPosition;
    }

    public void setCdsPosition(Integer cdsPosition) {
        this.cdsPosition = cdsPosition;
    }

    public void setAaPosition(Integer aaPosition) {
        this.aaPosition = aaPosition;
    }

    public void setAaChange(String aaChange) {
        this.aaChange = aaChange;
    }

    public String getGeneName() {
        return geneName;
    }

    public String getEnsemblGeneId() {
        return ensemblGeneId;
    }

    public String getEnsemblTranscriptId() {
        return ensemblTranscriptId;
    }

    public Integer getRelativePosition() {
        return relativePosition;
    }

    public String getCodon() {
        return codon;
    }

    public String getStrand() {
        return strand;
    }

    public String getBiotype() {
        return biotype;
    }

    public Integer getcDnaPosition() {
        return cDnaPosition;
    }

    public Integer getCdsPosition() {
        return cdsPosition;
    }

    public Integer getAaPosition() {
        return aaPosition;
    }

    public String getAaChange() {
        return aaChange;
    }

    public List<Integer> getSoAccessions() {
        return soAccessions;
    }

    public void setSoAccessions(List<Integer> soAccessions) {
        this.soAccessions = soAccessions;
    }

    public Score getSifts() {
        return sifts;
    }

    public void setSifts(Score sifts) {
        this.sifts = sifts;
    }

    public Score getPolyphen() {
        return polyphen;
    }

    public void setPolyphen(Score polyphen) {
        this.polyphen = polyphen;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConsequenceType that = (ConsequenceType) o;

        if (geneName != null ? !geneName.equals(that.geneName) : that.geneName != null) return false;
        if (ensemblGeneId != null ? !ensemblGeneId.equals(that.ensemblGeneId) : that.ensemblGeneId != null)
            return false;
        if (ensemblTranscriptId != null ? !ensemblTranscriptId
                .equals(that.ensemblTranscriptId) : that.ensemblTranscriptId != null) return false;
        if (strand != null ? !strand.equals(that.strand) : that.strand != null) return false;
        if (biotype != null ? !biotype.equals(that.biotype) : that.biotype != null) return false;
        if (cDnaPosition != null ? !cDnaPosition.equals(that.cDnaPosition) : that.cDnaPosition != null) return false;
        if (cdsPosition != null ? !cdsPosition.equals(that.cdsPosition) : that.cdsPosition != null) return false;
        if (aaPosition != null ? !aaPosition.equals(that.aaPosition) : that.aaPosition != null) return false;
        if (aaChange != null ? !aaChange.equals(that.aaChange) : that.aaChange != null) return false;
        if (codon != null ? !codon.equals(that.codon) : that.codon != null) return false;
        if (sifts != null ? !sifts.equals(that.sifts) : that.sifts != null) return false;
        if (polyphen != null ? !polyphen.equals(that.polyphen) : that.polyphen != null) return false;
        if (soAccessions != null ? !soAccessions.equals(that.soAccessions) : that.soAccessions != null) return false;
        return relativePosition != null ? relativePosition
                .equals(that.relativePosition) : that.relativePosition == null;
    }

    @Override
    public int hashCode() {
        int result = geneName != null ? geneName.hashCode() : 0;
        result = 31 * result + (ensemblGeneId != null ? ensemblGeneId.hashCode() : 0);
        result = 31 * result + (ensemblTranscriptId != null ? ensemblTranscriptId.hashCode() : 0);
        result = 31 * result + (strand != null ? strand.hashCode() : 0);
        result = 31 * result + (biotype != null ? biotype.hashCode() : 0);
        result = 31 * result + (cDnaPosition != null ? cDnaPosition.hashCode() : 0);
        result = 31 * result + (cdsPosition != null ? cdsPosition.hashCode() : 0);
        result = 31 * result + (aaPosition != null ? aaPosition.hashCode() : 0);
        result = 31 * result + (aaChange != null ? aaChange.hashCode() : 0);
        result = 31 * result + (codon != null ? codon.hashCode() : 0);
        result = 31 * result + (sifts != null ? sifts.hashCode() : 0);
        result = 31 * result + (polyphen != null ? polyphen.hashCode() : 0);
        result = 31 * result + (soAccessions != null ? soAccessions.hashCode() : 0);
        result = 31 * result + (relativePosition != null ? relativePosition.hashCode() : 0);
        return result;
    }
}