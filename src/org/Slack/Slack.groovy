#!/usr/bin/env groovy

package org.Slack
import groovy.json.JsonOutput



class Slack {

  public Slack() { /* empty */ }

  def sendPipelineInfo(body) {
    def payloads = []
    def pipelineTitle = [[
      title: "${body.jobName}, build #${body.buildNumber}",
      title_link: "${body.title_link}",
      color: "primary",
      text: "buildstarted by\n${body.author}",
      "mrkdwn_in": ["fields"],
      fields: [
        [
          title: "Branch",
          value: "${body.branch}",
          short: true
        ],
        [
          title: "Last Commit",
          value: "${body.message}",
          short: true
        ]
      ]
    ]]
    def title = JsonOutput.toJson([
        channel: "${body.channel}",
        username: "Jenkins",
        attachments: pipelineTitle 
    ])
    payloads.add(title)

    for (int i = 0; i < body.stageNames.size(); i++){
      def stage = [[
        color: "primary",
        "author_name": "${body.stageNames[i]}: Not started",
      ]]
      def stageMessage = JsonOutput.toJson([
          channel: "${body.channel}",
          username: "Jenkins",
          attachments: stage 
      ])
      payloads.add(stageMessage)
    }

    return payloads

  }

  def sendStageRunning(channel, name, ts) {
    def stage = [[
      color: "#cccc00",
      "author_name": "${name}: running",
      "author_icon": "https://images.atomist.com/rug/pulsating-circle.gif"
    ]]
    def payload = JsonOutput.toJson([
        ts: "${ts}",
        channel: "${channel}",
        username: "Jenkins",
        attachments: stage  
    ])

    return payload
  }

  def sendStageSuccess(channel, name, ts) {
    def stage = [[
      color: "#45B254",
      "author_name": "${name}: passed!",
      "author_icon": "https://images.atomist.com/rug/check-circle.png"
    ]]
    def payload = JsonOutput.toJson([
        ts: "${ts}",
        channel: "${channel}",
        username: "Jenkins",
        attachments: stage  
    ])

    return payload
  }
  def sendPipelineFailure(channel, name, ts, log) {
    def stage = [[
      color: "danger",
      "author_name": "${name}: failed",
      //"author_icon": ""
      "text": "```${log}```"
    ]]
    def payload = JsonOutput.toJson([
        ts: "${ts}",
        channel: "${channel}",
        username: "Jenkins",
        attachments: stage  
    ])

    return payload
  }
}

