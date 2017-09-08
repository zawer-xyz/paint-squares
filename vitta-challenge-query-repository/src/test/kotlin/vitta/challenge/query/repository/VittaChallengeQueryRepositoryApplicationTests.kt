package vitta.challenge.query.repository

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import vitta.challenge.domain.Painted
import vitta.challenge.domain.Point
import vitta.challenge.domain.Square
import vitta.challenge.domain.TerritoryId
import vitta.challenge.representation.PointRepresentation
import vitta.challenge.representation.TerritoryRepresentation

@RunWith(SpringRunner::class)
@SpringBootTest
@ContextConfiguration(classes = arrayOf(MongoConfig::class))
class VittaChallengeQueryRepositoryApplicationTests {

    @Autowired
    lateinit var territoryRepository: TerritoryRepository

    @Autowired
    lateinit var squareRepository: SquareRepository

    @Test
    fun findTerritoriesById() {
        val territoryId = TerritoryId()
        val territory = TerritoryRepresentation(id = territoryId.value,
                                                name = "First Name",
                                                start = PointRepresentation(x = 0, y = 0),
                                                end = PointRepresentation(x = 40, y = 40)
        )
        territoryRepository.save(territory).then().block()
        val territoryFetched = territoryRepository.findById(territoryId.value).block()
        Assert.assertEquals(territory.name, territoryFetched!!.name)
        Assert.assertEquals(1600, territoryFetched.area)
        Assert.assertEquals(0, territoryFetched.painted_area)
    }

    @Test
    fun findSquareByPoint() {
        val square = Square(null, Point(x = 2, y = 4), Painted(true))
        squareRepository.save(square).then().block()
        val squareFetched = squareRepository.findOneByPoint(Point(2,4)).block()
        Assert.assertEquals(Painted(true), squareFetched!!.painted)
    }
}

