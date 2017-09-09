package vitta.challenge.query.event.handler

import br.com.zup.eventsourcing.core.AggregateId
import br.com.zup.eventsourcing.core.AggregateVersion
import br.com.zup.eventsourcing.core.Event
import br.com.zup.eventsourcing.core.EventHandler
import br.com.zup.eventsourcing.core.MetaData
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.stereotype.Service
import vitta.challenge.domain.SquarePainted
import vitta.challenge.domain.Territory
import vitta.challenge.domain.TerritoryCreated
import vitta.challenge.domain.TerritoryDeleted
import vitta.challenge.query.repository.TerritoryRepository
import vitta.challenge.representation.PointRepresentation
import vitta.challenge.representation.TerritoryRepresentation


@Service
class TerritoryEventHandler(val territoryRepository: TerritoryRepository,
                            val template: ReactiveMongoTemplate) : EventHandler {
    override fun handle(aggregateId: AggregateId, event: Event, metaData: MetaData, version: AggregateVersion) {
        when (event) {
            is TerritoryCreated -> execute(aggregateId, event, version)
            is TerritoryDeleted -> execute(aggregateId, event, version)
            is SquarePainted -> execute(aggregateId, event, version)
        }
    }

    private fun execute(aggregateId: AggregateId, event: TerritoryCreated, version: AggregateVersion) {

        territoryRepository.save(TerritoryRepresentation.fromDomain(Territory(territoryId = event.territoryId,
                                                                              name = event.name,
                                                                              start = event.start,
                                                                              end = event.end
        )
        )
        ).subscribe()
    }

    private fun execute(aggregateId: AggregateId, event: TerritoryDeleted, version: AggregateVersion) {
        territoryRepository.deleteById(aggregateId.value).subscribe()
    }

    private fun execute(aggregateId: AggregateId, event: SquarePainted, version: AggregateVersion) {
        territoryRepository.findById(aggregateId.value).subscribe {
            it.painted_points.add(PointRepresentation(event.point.x, event.point.y))
            territoryRepository.save(it).subscribe()
        }
    }

}