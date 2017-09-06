package vitta.challenge.query.repository.repositories

import br.com.zup.eventsourcing.core.AggregateId
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import vitta.challenge.domain.Territory

interface TerritoryRepository : ReactiveMongoRepository<Territory, AggregateId> {

 //   fun findOneId(id: AggregateId): Mono<Territory>

}