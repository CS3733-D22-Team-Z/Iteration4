package edu.wpi.cs3733.D22.teamZ.database;

import edu.wpi.cs3733.D22.teamZ.entity.LabServiceRequest;
import edu.wpi.cs3733.D22.teamZ.entity.ServiceRequest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LabRequestControlCSV extends ControlCSV {
	private final IServiceRequestDAO requestDAO = new ServiceRequestDAOImpl();
	private final String[] headers = {"requestID", "labType"};

	public LabRequestControlCSV(File path) {
		this.setPath(path);
	}

	public void writeLabRequestCSV(List<LabServiceRequest> in) {
		writeCSV(objToData(in), headers);
	}

	public List<LabServiceRequest> readLabRequestCSV() throws IOException {
		return dataToObj(readCSV());
	}

	private List<LabServiceRequest> dataToObj(List<List<String>> in) {
		List<LabServiceRequest> labRequestList = new ArrayList<>();

		for(List<String> entry : in) {
			String requestID = entry.get(0);
			String labTypeStr = entry.get(1);

			ServiceRequest request = requestDAO.getServiceRequestByID(requestID);

			labRequestList.add(new LabServiceRequest(
				requestID,
				request.getStatus(),
				request.getIssuer(),
				request.getHandler(),
				request.getTargetLocation(),
				labTypeStr
			));
		}

		return labRequestList;
	}

	private List<List<String>> objToData(List<LabServiceRequest> in) {
		List<List<String>> data = new ArrayList<>();

		for(LabServiceRequest request : in) {
			List<String> entry = new ArrayList<>(List.of(new String[] {
					request.getRequestID(), request.getLabType()
			}));

			data.add(entry);
		}

		return data;
	}
}
