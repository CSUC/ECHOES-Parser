/**
 * 
 */
package org.Morphia.Core.dao.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.Morphia.Core.client.MorphiaEchoes;
import org.Morphia.Core.dao.HarvestStatus;
import org.Morphia.Core.dao.HarvestedItemsDAO;
import org.Morphia.Core.entities.HarvestedCollectionConfig;
import org.Morphia.Core.entities.HarvestedItems;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;

/**
 * @author amartinez
 *
 */
public class HarvestedItemsDAOImplTest extends TestCase {

	private static Logger logger = LogManager.getLogger(HarvestedItemsDAOImplTest.class);

	private MorphiaEchoes echoes = new MorphiaEchoes("echoes");
	private HarvestedItemsDAO itemsDAO = new HarvestedItemsDAOImpl(HarvestedItems.class, echoes.getDatastore());

	private HarvestStatus status = HarvestStatus.READY;
	
	private HarvestedCollectionConfig harvested = new HarvestedCollectionConfig();
	private String harvest_uuid = UUID.randomUUID().toString();

	private HarvestedItems items = new HarvestedItems();
	private String item_id = UUID.randomUUID().toString();
	
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	@Before
	protected void setUp() throws Exception {
		super.setUp();
		
		harvested.setId(harvest_uuid);

		harvested.setOaisetid("col_10803_78");
		harvested.setOaisource("http://tdx.cat/oai/request");
		harvested.setHarcestmessage(status.getDescription());
		harvested.setMetadataconfig("oai_dc");
		harvested.setHarveststatus(status.getValue());
		harvested.setHarveststarttime(new Date());
		harvested.setLastharvested(null);
		harvested.setXsdconfig(null);

		items.setId(item_id);
		items.setLastharavested(new Date());
		items.setHarvestedcollection(harvested);

		echoes.getDatastore().save(Arrays.asList(harvested, items));
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	@After
	protected void tearDown() throws Exception {
		super.tearDown();
		if (Objects.nonNull(echoes.getDatastore()))
			echoes.getDatastore().getMongo().dropDatabase("echoes");
	}

	/**
	 * Test method for {@link org.Morphia.Core.dao.impl.HarvestedItemsDAOImpl#findAll()}.
	 */
	@Test
	public void testFindAll() {
		List<HarvestedItems> result = itemsDAO.findAll();

		assertNotNull(result);
		assertEquals(1, result.size());
		
		result.forEach(h -> {logger.info(h.toJson());});
	}

	/**
	 * Test method for {@link org.Morphia.Core.dao.impl.HarvestedItemsDAOImpl#findById(java.lang.String)}.
	 */
	@Test
	public void testFindById() {
		HarvestedItems result = itemsDAO.findById(item_id);

		assertNotNull(result);
		assertEquals(item_id, result.getId());
		
		logger.info(result.toJson());
	}

	/**
	 * Test method for {@link org.Morphia.Core.dao.impl.HarvestedItemsDAOImpl#findAll(org.Morphia.Core.entities.HarvestedCollectionConfig)}.
	 */
	@Test
	public void testFindAllHarvestedCollectionConfig() {
		List<HarvestedItems> result = itemsDAO.findAll(harvest_uuid);

		assertNotNull(result);
		assertEquals(1, result.size());
		
		result.forEach(h -> {
			assertTrue(harvest_uuid.equals(h.getHarvestedcollection()));
			logger.info(h.toJson());
		});
	}

	/**
	 * Test method for {@link org.Morphia.Core.dao.impl.HarvestedItemsDAOImpl#findAll(java.lang.String)}.
	 */
	@Test
	public void testFindAllString() {
		List<HarvestedItems> result = itemsDAO.findAll(harvested);

		assertNotNull(result);
		assertEquals(1, result.size());
		
		result.forEach(h -> {
			assertTrue(harvest_uuid.equals(h.getHarvestedcollection()));
			logger.info(h.toJson());
		});
	}

}
