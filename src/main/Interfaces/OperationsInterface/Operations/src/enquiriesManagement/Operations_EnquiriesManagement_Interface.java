package Interfaces.OperationsInterface.Operations.src.enquiriesManagement;

import java.util.List;

public interface Operations_EnquiriesManagement_Interface {
    // Retrieve all enquiries or filter by status
    List<MarketingEnquiry> getAllEnquiries();
    List<MarketingEnquiry> getEnquiries(String status);

    // Retrieve specific enquiry details
    MarketingEnquiry getEnquiryDetails(String enquiryId);
}
