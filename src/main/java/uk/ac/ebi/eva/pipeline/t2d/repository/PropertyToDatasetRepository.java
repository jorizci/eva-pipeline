package uk.ac.ebi.eva.pipeline.t2d.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.eva.pipeline.t2d.entity.PropertyToDataset;
import uk.ac.ebi.eva.pipeline.t2d.entity.embedded.id.PropertyIdDatasetId;

@Repository
@Transactional
public interface PropertyToDatasetRepository extends CrudRepository<PropertyToDataset, PropertyIdDatasetId> {
}
