package com.example.Sim.Scripts.ScriptGenerator.FunctionCalls;

import com.example.Sim.Scripts.ScriptGenerator.FunctionCall;
import lombok.Getter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;


@Getter
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ChangePlayerSkillNode extends FunctionCall {
    @XmlAttribute
    public String Skill;
    public String SkillChange;

    public ChangePlayerSkillNode() {
    }

    public ChangePlayerSkillNode(String text, String nextNode, String skill, String skillChange) {
        Skill = skill;
        SkillChange = skillChange;
        this.Function = "ChangePlayerSkill";
        this.NextNode = nextNode;
        this.Text = text;
    }

}

