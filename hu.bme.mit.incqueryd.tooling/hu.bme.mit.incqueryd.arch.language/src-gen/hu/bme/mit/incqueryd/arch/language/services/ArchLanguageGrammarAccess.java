/*
* generated by Xtext
*/
package hu.bme.mit.incqueryd.arch.language.services;

import com.google.inject.Singleton;
import com.google.inject.Inject;

import java.util.List;

import org.eclipse.xtext.*;
import org.eclipse.xtext.service.GrammarProvider;
import org.eclipse.xtext.service.AbstractElementFinder.*;

import org.eclipse.xtext.common.services.TerminalsGrammarAccess;

@Singleton
public class ArchLanguageGrammarAccess extends AbstractGrammarElementFinder {
	
	
	public class ConfigurationElements extends AbstractParserRuleElementFinder {
		private final ParserRule rule = (ParserRule) GrammarUtil.findRuleForName(getGrammar(), "Configuration");
		private final Group cGroup = (Group)rule.eContents().get(1);
		private final Assignment cRecipeImportsAssignment_0 = (Assignment)cGroup.eContents().get(0);
		private final RuleCall cRecipeImportsRecipeImportParserRuleCall_0_0 = (RuleCall)cRecipeImportsAssignment_0.eContents().get(0);
		private final Assignment cMappingsAssignment_1 = (Assignment)cGroup.eContents().get(1);
		private final RuleCall cMappingsInfrastructureMappingParserRuleCall_1_0 = (RuleCall)cMappingsAssignment_1.eContents().get(0);
		
		//Configuration:
		//	recipeImports+=RecipeImport* mappings+=InfrastructureMapping*;
		public ParserRule getRule() { return rule; }

		//recipeImports+=RecipeImport* mappings+=InfrastructureMapping*
		public Group getGroup() { return cGroup; }

		//recipeImports+=RecipeImport*
		public Assignment getRecipeImportsAssignment_0() { return cRecipeImportsAssignment_0; }

		//RecipeImport
		public RuleCall getRecipeImportsRecipeImportParserRuleCall_0_0() { return cRecipeImportsRecipeImportParserRuleCall_0_0; }

		//mappings+=InfrastructureMapping*
		public Assignment getMappingsAssignment_1() { return cMappingsAssignment_1; }

		//InfrastructureMapping
		public RuleCall getMappingsInfrastructureMappingParserRuleCall_1_0() { return cMappingsInfrastructureMappingParserRuleCall_1_0; }
	}

	public class RecipeImportElements extends AbstractParserRuleElementFinder {
		private final ParserRule rule = (ParserRule) GrammarUtil.findRuleForName(getGrammar(), "RecipeImport");
		private final Group cGroup = (Group)rule.eContents().get(1);
		private final Keyword cRecipeKeyword_0 = (Keyword)cGroup.eContents().get(0);
		private final Assignment cImportURIAssignment_1 = (Assignment)cGroup.eContents().get(1);
		private final RuleCall cImportURISTRINGTerminalRuleCall_1_0 = (RuleCall)cImportURIAssignment_1.eContents().get(0);
		
		//RecipeImport:
		//	"recipe" importURI=STRING;
		public ParserRule getRule() { return rule; }

		//"recipe" importURI=STRING
		public Group getGroup() { return cGroup; }

		//"recipe"
		public Keyword getRecipeKeyword_0() { return cRecipeKeyword_0; }

		//importURI=STRING
		public Assignment getImportURIAssignment_1() { return cImportURIAssignment_1; }

		//STRING
		public RuleCall getImportURISTRINGTerminalRuleCall_1_0() { return cImportURISTRINGTerminalRuleCall_1_0; }
	}

	public class InfrastructureMappingElements extends AbstractParserRuleElementFinder {
		private final ParserRule rule = (ParserRule) GrammarUtil.findRuleForName(getGrammar(), "InfrastructureMapping");
		private final Group cGroup = (Group)rule.eContents().get(1);
		private final Keyword cUseKeyword_0 = (Keyword)cGroup.eContents().get(0);
		private final Assignment cMachineAssignment_1 = (Assignment)cGroup.eContents().get(1);
		private final RuleCall cMachineMachineParserRuleCall_1_0 = (RuleCall)cMachineAssignment_1.eContents().get(0);
		private final Keyword cForKeyword_2 = (Keyword)cGroup.eContents().get(2);
		private final Assignment cRolesAssignment_3 = (Assignment)cGroup.eContents().get(3);
		private final RuleCall cRolesRoleParserRuleCall_3_0 = (RuleCall)cRolesAssignment_3.eContents().get(0);
		private final Group cGroup_4 = (Group)cGroup.eContents().get(4);
		private final Keyword cCommaKeyword_4_0 = (Keyword)cGroup_4.eContents().get(0);
		private final Assignment cRolesAssignment_4_1 = (Assignment)cGroup_4.eContents().get(1);
		private final RuleCall cRolesRoleParserRuleCall_4_1_0 = (RuleCall)cRolesAssignment_4_1.eContents().get(0);
		
		//InfrastructureMapping:
		//	"use" machine=Machine "for" roles+=Role ("," roles+=Role)*;
		public ParserRule getRule() { return rule; }

		//"use" machine=Machine "for" roles+=Role ("," roles+=Role)*
		public Group getGroup() { return cGroup; }

		//"use"
		public Keyword getUseKeyword_0() { return cUseKeyword_0; }

		//machine=Machine
		public Assignment getMachineAssignment_1() { return cMachineAssignment_1; }

		//Machine
		public RuleCall getMachineMachineParserRuleCall_1_0() { return cMachineMachineParserRuleCall_1_0; }

		//"for"
		public Keyword getForKeyword_2() { return cForKeyword_2; }

		//roles+=Role
		public Assignment getRolesAssignment_3() { return cRolesAssignment_3; }

		//Role
		public RuleCall getRolesRoleParserRuleCall_3_0() { return cRolesRoleParserRuleCall_3_0; }

		//("," roles+=Role)*
		public Group getGroup_4() { return cGroup_4; }

		//","
		public Keyword getCommaKeyword_4_0() { return cCommaKeyword_4_0; }

		//roles+=Role
		public Assignment getRolesAssignment_4_1() { return cRolesAssignment_4_1; }

		//Role
		public RuleCall getRolesRoleParserRuleCall_4_1_0() { return cRolesRoleParserRuleCall_4_1_0; }
	}

	public class MachineElements extends AbstractParserRuleElementFinder {
		private final ParserRule rule = (ParserRule) GrammarUtil.findRuleForName(getGrammar(), "Machine");
		private final Assignment cIpAssignment = (Assignment)rule.eContents().get(1);
		private final RuleCall cIpIPTerminalRuleCall_0 = (RuleCall)cIpAssignment.eContents().get(0);
		
		//Machine:
		//	ip=IP;
		public ParserRule getRule() { return rule; }

		//ip=IP
		public Assignment getIpAssignment() { return cIpAssignment; }

		//IP
		public RuleCall getIpIPTerminalRuleCall_0() { return cIpIPTerminalRuleCall_0; }
	}

	public class RoleElements extends AbstractParserRuleElementFinder {
		private final ParserRule rule = (ParserRule) GrammarUtil.findRuleForName(getGrammar(), "Role");
		private final Alternatives cAlternatives = (Alternatives)rule.eContents().get(1);
		private final RuleCall cReteRoleParserRuleCall_0 = (RuleCall)cAlternatives.eContents().get(0);
		private final RuleCall cCacheRoleParserRuleCall_1 = (RuleCall)cAlternatives.eContents().get(1);
		
		//Role:
		//	ReteRole | CacheRole;
		public ParserRule getRule() { return rule; }

		//ReteRole | CacheRole
		public Alternatives getAlternatives() { return cAlternatives; }

		//ReteRole
		public RuleCall getReteRoleParserRuleCall_0() { return cReteRoleParserRuleCall_0; }

		//CacheRole
		public RuleCall getCacheRoleParserRuleCall_1() { return cCacheRoleParserRuleCall_1; }
	}

	public class ReteRoleElements extends AbstractParserRuleElementFinder {
		private final ParserRule rule = (ParserRule) GrammarUtil.findRuleForName(getGrammar(), "ReteRole");
		private final Assignment cNodeRecipeAssignment = (Assignment)rule.eContents().get(1);
		private final CrossReference cNodeRecipeReteNodeRecipeCrossReference_0 = (CrossReference)cNodeRecipeAssignment.eContents().get(0);
		private final RuleCall cNodeRecipeReteNodeRecipeIDTerminalRuleCall_0_1 = (RuleCall)cNodeRecipeReteNodeRecipeCrossReference_0.eContents().get(1);
		
		//ReteRole:
		//	nodeRecipe=[ReteNodeRecipe];
		public ParserRule getRule() { return rule; }

		//nodeRecipe=[ReteNodeRecipe]
		public Assignment getNodeRecipeAssignment() { return cNodeRecipeAssignment; }

		//[ReteNodeRecipe]
		public CrossReference getNodeRecipeReteNodeRecipeCrossReference_0() { return cNodeRecipeReteNodeRecipeCrossReference_0; }

		//ID
		public RuleCall getNodeRecipeReteNodeRecipeIDTerminalRuleCall_0_1() { return cNodeRecipeReteNodeRecipeIDTerminalRuleCall_0_1; }
	}

	public class CacheRoleElements extends AbstractParserRuleElementFinder {
		private final ParserRule rule = (ParserRule) GrammarUtil.findRuleForName(getGrammar(), "CacheRole");
		private final Group cGroup = (Group)rule.eContents().get(1);
		private final Action cCacheRoleAction_0 = (Action)cGroup.eContents().get(0);
		private final Keyword cCacheKeyword_1 = (Keyword)cGroup.eContents().get(1);
		
		//CacheRole:
		//	{CacheRole} "cache";
		public ParserRule getRule() { return rule; }

		//{CacheRole} "cache"
		public Group getGroup() { return cGroup; }

		//{CacheRole}
		public Action getCacheRoleAction_0() { return cCacheRoleAction_0; }

		//"cache"
		public Keyword getCacheKeyword_1() { return cCacheKeyword_1; }
	}
	
	
	private ConfigurationElements pConfiguration;
	private RecipeImportElements pRecipeImport;
	private InfrastructureMappingElements pInfrastructureMapping;
	private MachineElements pMachine;
	private RoleElements pRole;
	private ReteRoleElements pReteRole;
	private CacheRoleElements pCacheRole;
	private TerminalRule tIP;
	
	private final Grammar grammar;

	private TerminalsGrammarAccess gaTerminals;

	@Inject
	public ArchLanguageGrammarAccess(GrammarProvider grammarProvider,
		TerminalsGrammarAccess gaTerminals) {
		this.grammar = internalFindGrammar(grammarProvider);
		this.gaTerminals = gaTerminals;
	}
	
	protected Grammar internalFindGrammar(GrammarProvider grammarProvider) {
		Grammar grammar = grammarProvider.getGrammar(this);
		while (grammar != null) {
			if ("hu.bme.mit.incqueryd.arch.language.ArchLanguage".equals(grammar.getName())) {
				return grammar;
			}
			List<Grammar> grammars = grammar.getUsedGrammars();
			if (!grammars.isEmpty()) {
				grammar = grammars.iterator().next();
			} else {
				return null;
			}
		}
		return grammar;
	}
	
	
	public Grammar getGrammar() {
		return grammar;
	}
	

	public TerminalsGrammarAccess getTerminalsGrammarAccess() {
		return gaTerminals;
	}

	
	//Configuration:
	//	recipeImports+=RecipeImport* mappings+=InfrastructureMapping*;
	public ConfigurationElements getConfigurationAccess() {
		return (pConfiguration != null) ? pConfiguration : (pConfiguration = new ConfigurationElements());
	}
	
	public ParserRule getConfigurationRule() {
		return getConfigurationAccess().getRule();
	}

	//RecipeImport:
	//	"recipe" importURI=STRING;
	public RecipeImportElements getRecipeImportAccess() {
		return (pRecipeImport != null) ? pRecipeImport : (pRecipeImport = new RecipeImportElements());
	}
	
	public ParserRule getRecipeImportRule() {
		return getRecipeImportAccess().getRule();
	}

	//InfrastructureMapping:
	//	"use" machine=Machine "for" roles+=Role ("," roles+=Role)*;
	public InfrastructureMappingElements getInfrastructureMappingAccess() {
		return (pInfrastructureMapping != null) ? pInfrastructureMapping : (pInfrastructureMapping = new InfrastructureMappingElements());
	}
	
	public ParserRule getInfrastructureMappingRule() {
		return getInfrastructureMappingAccess().getRule();
	}

	//Machine:
	//	ip=IP;
	public MachineElements getMachineAccess() {
		return (pMachine != null) ? pMachine : (pMachine = new MachineElements());
	}
	
	public ParserRule getMachineRule() {
		return getMachineAccess().getRule();
	}

	//Role:
	//	ReteRole | CacheRole;
	public RoleElements getRoleAccess() {
		return (pRole != null) ? pRole : (pRole = new RoleElements());
	}
	
	public ParserRule getRoleRule() {
		return getRoleAccess().getRule();
	}

	//ReteRole:
	//	nodeRecipe=[ReteNodeRecipe];
	public ReteRoleElements getReteRoleAccess() {
		return (pReteRole != null) ? pReteRole : (pReteRole = new ReteRoleElements());
	}
	
	public ParserRule getReteRoleRule() {
		return getReteRoleAccess().getRule();
	}

	//CacheRole:
	//	{CacheRole} "cache";
	public CacheRoleElements getCacheRoleAccess() {
		return (pCacheRole != null) ? pCacheRole : (pCacheRole = new CacheRoleElements());
	}
	
	public ParserRule getCacheRoleRule() {
		return getCacheRoleAccess().getRule();
	}

	//terminal IP returns EString:
	//	INT "." INT "." INT "." INT;
	public TerminalRule getIPRule() {
		return (tIP != null) ? tIP : (tIP = (TerminalRule) GrammarUtil.findRuleForName(getGrammar(), "IP"));
	} 

	//terminal ID returns ecore::EString:
	//	"^"? ("a".."z" | "A".."Z" | "_") ("a".."z" | "A".."Z" | "_" | "0".."9")*;
	public TerminalRule getIDRule() {
		return gaTerminals.getIDRule();
	} 

	//terminal INT returns ecore::EInt:
	//	"0".."9"+;
	public TerminalRule getINTRule() {
		return gaTerminals.getINTRule();
	} 

	//terminal STRING returns ecore::EString:
	//	"\"" ("\\" ("b" | "t" | "n" | "f" | "r" | "u" | "\"" | "\'" | "\\") | !("\\" | "\""))* "\"" | "\'" ("\\" ("b" | "t" |
	//	"n" | "f" | "r" | "u" | "\"" | "\'" | "\\") | !("\\" | "\'"))* "\'";
	public TerminalRule getSTRINGRule() {
		return gaTerminals.getSTRINGRule();
	} 

	//terminal ML_COMMENT returns ecore::EString:
	//	"/ *"->"* /";
	public TerminalRule getML_COMMENTRule() {
		return gaTerminals.getML_COMMENTRule();
	} 

	//terminal SL_COMMENT returns ecore::EString:
	//	"//" !("\n" | "\r")* ("\r"? "\n")?;
	public TerminalRule getSL_COMMENTRule() {
		return gaTerminals.getSL_COMMENTRule();
	} 

	//terminal WS returns ecore::EString:
	//	(" " | "\t" | "\r" | "\n")+;
	public TerminalRule getWSRule() {
		return gaTerminals.getWSRule();
	} 

	//terminal ANY_OTHER returns ecore::EString:
	//	.;
	public TerminalRule getANY_OTHERRule() {
		return gaTerminals.getANY_OTHERRule();
	} 
}
