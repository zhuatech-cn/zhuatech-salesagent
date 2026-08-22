/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.salesagent.controller;

import cn.zhuatech.salesagent.agent.AgentRuntime;
import cn.zhuatech.salesagent.common.ApiResponse;
import cn.zhuatech.salesagent.dto.SalesAgentDto.*;
import cn.zhuatech.salesagent.service.FollowUpCadenceService;
import cn.zhuatech.salesagent.service.LeadActionService;
import cn.zhuatech.salesagent.service.SalesAgentService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/shopfloor")
@PreAuthorize("hasAnyRole('DOMAIN_USER','ADMIN')")
public class WorkspaceController {
    private final SalesAgentService service;
    private final AgentRuntime runtime;
    private final LeadActionService leadAction;
    private final FollowUpCadenceService followUpCadence;

    public WorkspaceController(SalesAgentService service, AgentRuntime runtime, LeadActionService leadAction, FollowUpCadenceService followUpCadence) {
        this.service = service;
        this.runtime = runtime;
        this.leadAction = leadAction;
        this.followUpCadence = followUpCadence;
    }

    @GetMapping("/dashboard")
    public ApiResponse<Dashboard> dashboard() { return ApiResponse.ok(service.shopfloorDashboard()); }

    @PostMapping("/work-orders/{id}/reports")
    public ApiResponse<ReportResult> report(@PathVariable Long id, @Valid @RequestBody ReportRequest request) {
        return ApiResponse.ok("反馈提交成功", service.report(id, request));
    }

    @PostMapping("/agent-preview")
    public ApiResponse<AgentRuntime.AgentResult> preview(@RequestBody Map<String, String> body) {
        return ApiResponse.ok(runtime.run(new AgentRuntime.AgentRequest(body.getOrDefault("objective", "准备客户拜访"), Map.of("mode", "demo"))));
    }

    @PostMapping("/lead-action")
    public ApiResponse<LeadActionService.ActionResult> plan(@Valid @RequestBody LeadActionService.ActionRequest request) {
        return ApiResponse.ok("销售行动规划完成", leadAction.plan(request));
    }

    @PostMapping("/follow-up-cadence")
    public ApiResponse<FollowUpCadenceService.CadenceResult> planCadence(@Valid @RequestBody FollowUpCadenceService.CadenceRequest request) {
        return ApiResponse.ok("客户跟进节奏生成完成", followUpCadence.plan(request));
    }
}
