class PagesController < ApplicationController
  protect_from_forgery

  helper_method :signed_in?

  def index
    @title = "Home"
  end

  def login
    user = User.find_all_by_name(params["user"]) && User.find_all_by_pass(params["pass"])
    if user.count == 1
      respond = {log: true, token: user.first.id}
    else
      respond = {log: false}
    end
    respond_to do |format|
      format.json { render :json => respond }
    end
  end

  def note
    begin
      user = User.find(params["token"])
      #When the user is new
      if user.text.nil?
        user.text = Text.create(title: "New Note!", body: "...")
      end
      respond = {log: true, title: user.text.title, body: user.text.body}
    rescue
      respond = {log: false}
    end

    respond_to do |format|
      format.json { render :json => respond }
    end
  end

  def update_note
    begin
      user = User.find(params["token"])
      user.text.title = params["title"]
      user.text.body = params["body"]
      if user.text.save
        respond = {log: true, title: user.text.title, body: user.text.body}
      elsif
        respond = {log: false}
      end
    rescue
      respond = {log: false}
    end
    respond_to do |format|
      format.json { render :json => respond }
    end
  end


  def error
    @title = "Error"
  end


  def signed_in?
    false
  end
end
