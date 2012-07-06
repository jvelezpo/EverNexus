class PagesController < ApplicationController
  def index

  end

  def nota
    user = params[:user]

    nota = User.find_by_nombre(user).notas.last

    if nota.nil?
      archivo "No existe el usuario #{user}"
    else
      archivo nota.nota
    end
  end

  def questions
    send_file "#{Rails.root}/app/assets/raw/questions.xml", :type => "application/xml", :disposition => 'inline'
  end

  private

  def archivo(texto)
    File.open("#{Rails.root}/app/assets/raw/nota.txt", "w") do |file|
      file.puts texto
    end
    send_file "#{Rails.root}/app/assets/raw/nota.txt", :type => "text; charset=utf-8", :disposition => 'inline'
  end

end
