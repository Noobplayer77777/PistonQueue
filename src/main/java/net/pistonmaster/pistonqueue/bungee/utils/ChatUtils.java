/*
 * #%L
 * PistonQueue
 * %%
 * Copyright (C) 2021 AlexProgrammerDE
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package net.pistonmaster.pistonqueue.bungee.utils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.pistonmaster.pistonqueue.bungee.QueueAPI;

public final class ChatUtils {
    private ChatUtils() {
    }

    public static String parseToString(String str) {
        return ChatColor.translateAlternateColorCodes('&', parseText(str));
    }

    public static BaseComponent[] parseToComponent(String str) {
        return TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', parseText(str)));
    }

    private static String parseText(String text) {
        String returnedText = text;

        returnedText = returnedText.replace("%veteran%", String.valueOf(QueueAPI.getVeteranSize()));
        returnedText = returnedText.replace("%priority%", String.valueOf(QueueAPI.getPrioritySize()));
        returnedText = returnedText.replace("%regular%", String.valueOf(QueueAPI.getRegularSize()));

        return returnedText;
    }
}
