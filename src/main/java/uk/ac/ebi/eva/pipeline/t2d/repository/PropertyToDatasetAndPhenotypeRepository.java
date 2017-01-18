package uk.ac.ebi.eva.pipeline.t2d.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.eva.pipeline.t2d.entity.PropertyToDatasetAndPhenotype;
import uk.ac.ebi.eva.pipeline.t2d.entity.embedded.id.PropertyIdDatasetIdPhenotypeId;

@Repository
@Transactional
public interface PropertyToDatasetAndPhenotypeRepository extends CrudRepository<PropertyToDatasetAndPhenotype,
        PropertyIdDatasetIdPhenotypeId> {
}
