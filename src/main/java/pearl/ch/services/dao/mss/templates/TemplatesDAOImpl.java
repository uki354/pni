package pearl.ch.services.dao.mss.templates;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import pearl.ch.services.aws.AWSConfig;
import pearl.ch.services.config.annotations.ReadTransactional;
import pearl.ch.services.config.annotations.WriteTransactional;
import pearl.ch.services.dto.mss.templates.TemplatesDTO;
import pearl.ch.services.entity.mssdb.languages.Languages;
import pearl.ch.services.entity.mssdb.mss.MssTemplates;
import pearl.ch.services.entity.mssdb.mss.MssTypes;
import pearl.ch.services.entity.mssdb.mss.pk.MssTypesPK;
import pearl.ch.services.entity.mssdb.shops.Shops;
import pearl.ch.services.enums.TemplateState;

@Repository
@RequiredArgsConstructor
public class TemplatesDAOImpl implements TemplatesDAO {

	@PersistenceContext(unitName = "mssRead")
	private EntityManager entityManager;
	
	@PersistenceContext(unitName = "mssWrite")
	private EntityManager entityManagerWrite;

	private final AWSConfig awsConfig;

	@ReadTransactional
	public List<TemplatesDTO> getAll(int limit) {

		List<TemplatesDTO> mssTemplatesDTOList = new ArrayList<TemplatesDTO>();

		try (Session session = entityManager.unwrap(Session.class);){
			List<MssTemplates> mssTemplates;
			if (limit > 0)
				mssTemplates = session
						.createQuery("FROM MssTemplates ORDER BY mss_template_id DESC", MssTemplates.class)
						.setMaxResults(limit).getResultList();
			else
				mssTemplates = session
						.createQuery("FROM MssTemplates ORDER BY mss_template_id DESC", MssTemplates.class)
						.getResultList();

			for (MssTemplates allTemplate : mssTemplates) {

				TemplatesDTO mSSTemplateDTO = new TemplatesDTO();

				Shops shop = session.get(Shops.class, allTemplate.getShop_id());
				MssTypes mssType = session.get(MssTypes.class, new MssTypesPK(allTemplate.getMss_type_id(), allTemplate.getShop_id()));
				Languages language = session.get(Languages.class, allTemplate.getLanguage_id());

				mSSTemplateDTO.setMss_template_id(allTemplate.getMss_template_id());
				mSSTemplateDTO.setLanguage_name(language.getName());
				mSSTemplateDTO.setShop_name(shop.getName());
				mSSTemplateDTO.setMss_type_name(mssType.getName());
				mSSTemplateDTO.setName(allTemplate.getName());
				mSSTemplateDTO.setState(allTemplate.getState().toString());
				mSSTemplateDTO.setOnline_version_url(allTemplate.getOnline_version_url());
				mssTemplatesDTOList.add(mSSTemplateDTO);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return mssTemplatesDTOList;
	}

	@Override
	@ReadTransactional
	public List<MssTemplates> getTemplatesByState(TemplateState state) {
		return entityManager
				.createQuery("FROM MssTemplates WHERE state = :state ORDER BY mss_template_id DESC", MssTemplates.class)
				.setParameter("state", state).getResultList();
	}

	@Override
	@ReadTransactional
	public MssTemplates getTemplateById(int Id) {
			return  entityManager.find(MssTemplates.class, Id);
	}

	@Override
	@ReadTransactional
	public List<MssTemplates> getTemplatesByIds(List<Integer> ids) {
		return entityManager
				.createQuery("SELECT msst FROM MssTemplates msst WHERE msst.id IN (:ids)", MssTemplates.class)
				.setParameter("ids", ids).getResultList();

	}

	@Override
	@WriteTransactional
	public MssTemplates copyTemplate(int id, int userId) {

		MssTemplates templateCopy = new MssTemplates();

		try (Session session = entityManagerWrite.unwrap(Session.class);){

			MssTemplates templateSrc = session.get(MssTemplates.class, id);

			if (templateSrc != null) {

				// copy all properties except those in arguments list
				BeanUtils.copyProperties(templateSrc, templateCopy, "mss_template_id", "name", "status", "cruser",
						"crdate", "mail_from");

				// setting name of copied template to standard: COPY_srcName
				templateCopy.setName("COPY_" + templateSrc.getName());

				// set creation date and user who created template copy
				templateCopy.setCruser(userId);
				templateCopy.setCrdate(LocalDateTime.now().withNano(0));
				templateCopy.setState(TemplateState.EDITABLE);

				session.save(templateCopy);

				// Copying was successful.
				return templateCopy;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	@Override
	@WriteTransactional
	public int saveTemplate(MssTemplates mssTemplate, int userId) {

		int templateId = 0;

		try (Session session = entityManagerWrite.unwrap(Session.class);){
			
			mssTemplate.setCruser(userId);
			mssTemplate.setCrdate(LocalDateTime.now().withNano(0));
			mssTemplate.setState(TemplateState.EDITABLE);			
			templateId = (int) session.save(mssTemplate);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return templateId;
	}

	@Override
	@WriteTransactional
	public boolean updateTemplate(MssTemplates mssTemplate, int userId) {

		boolean response;

		try (Session session = entityManagerWrite.unwrap(Session.class);){
									
			mssTemplate.setUpduser(userId);
			mssTemplate.setUpddate(LocalDateTime.now().withNano(0));
			mssTemplate.setHtml_template(setOnlineVersionLink(mssTemplate.getHtml_template(), mssTemplate.getMss_template_id(), mssTemplate.getShop_id()));
			mssTemplate.setJson_template(setOnlineVersionLink(mssTemplate.getJson_template(), mssTemplate.getMss_template_id(), mssTemplate.getShop_id()));
			mssTemplate.setOnline_version_url(getOnlneVersionURL(mssTemplate));
			
			session.update(mssTemplate);
			session.flush();
			response = true;
		} catch (Exception e) {
			e.printStackTrace();
			response = false;
		}

		return response;
	}

	@Override
	@WriteTransactional
	public boolean updateTemplateState(int templateId, TemplateState state) {

		return entityManagerWrite.createQuery("UPDATE MssTemplates SET state = :state WHERE mss_template_id = :templateId")
				.setParameter("state", state).setParameter("templateId", templateId).executeUpdate() > 0;
	}
	

	private String setOnlineVersionLink(String template, int templateId, int shopId) {

		String pattern = "\\{\\{onlineVersion\\}\\}";
		String link = getCloudFrontForBillingBucket(shopId) + "/newsletter/"
				+ templateId + "/index.html";

		return template.replaceAll(pattern, link);

	}

	private String getOnlneVersionURL(MssTemplates mssTemplates) {
		return getCloudFrontForBillingBucket(mssTemplates.getShop_id()) + "/newsletter/"
				+ mssTemplates.getMss_template_id() + "/index.html";
	}
	

	private String getCloudFrontForBillingBucket(int shop_id) {
		switch (shop_id) {
		case 1:
			return awsConfig.s3PearlExportURL;
		case 2:
			return awsConfig.s3CasativoExportURL;
		case 3:
			;
			return awsConfig.s3NaturaExportURL;
		default:
			throw new IllegalArgumentException("Can not find cooresponding url for shop_id: " + shop_id);
		}
	}
}
